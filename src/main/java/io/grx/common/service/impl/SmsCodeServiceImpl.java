package io.grx.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import io.grx.common.service.SmsServiceFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grx.common.service.SmsCodeService;
import io.grx.common.service.SmsService;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.R;

@Service
public class SmsCodeServiceImpl implements SmsCodeService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private SmsServiceFactory smsServiceFactory;

    @Value("${sms.fakeCode}")
    private String fakeCode;

    @Value("${sms.enabled}")
    private boolean enabled;

    private static long MOBILE_EXPIRE_TIME = 60; // 1 minutes

    private static long CODE_EXPIRE_TIME = 60 * 10; // 10 minutes

    @Override
    public R getVerifyCode(final String mobile, final String type) {
        if (StringUtils.isBlank(mobile)) {
            return R.error("手机号错误");
        }

        String codeCacheKey = buildCacheKey(mobile + "_" + type);
        String mobileCacheKey = buildCacheKey(mobile + ":" + type);

        long lastTime = NumberUtils.toLong(cacheUtils.get(mobileCacheKey), 0);
        if (lastTime > 0 && (System.currentTimeMillis() - lastTime < MOBILE_EXPIRE_TIME * 1000)) {
            logger.debug("{} get code too frequenctly", mobileCacheKey);
            return R.error("获取验证码过于频繁, 请一分钟后再尝试");
        }

        String code = RandomStringUtils.randomNumeric(6);

        Map<String, String> params = new HashMap<>();

        boolean result = true;
        if (enabled) {
            result = smsServiceFactory.getSmsService().sendVerifyCode(mobile, type, code, params);
        }
        if (true) {
            cacheUtils.put(codeCacheKey, code, CODE_EXPIRE_TIME);
            cacheUtils.put(mobileCacheKey, String.valueOf(System.currentTimeMillis()),
                    MOBILE_EXPIRE_TIME);
            logger.info("sent code {} for {} to {}", code, type, mobile);
            R r = R.ok();
            //r.put("loginCode",code);
            return r;
        }

        return R.error("验证码发送失败, 请重试或联系客服");
    }

    @Override
    public boolean isVerifyCodeMatch(final String mobile, final String type, final String code) {
        String cacheKey = buildCacheKey(mobile + "_" + type);
        String codeInMemory = cacheUtils.get(cacheKey);

        boolean codeMatched = StringUtils.isNotBlank(code) && StringUtils.equals(code, codeInMemory);
        codeMatched = codeMatched || StringUtils.equals(code, fakeCode);
        if (!codeMatched) {
            logger.info("sms code not matched. mobile [{}], code [{}], user code [{}]", mobile, codeInMemory, code);
        } else {
//            cacheUtils.delete(cacheKey);
        }

        return codeMatched;
    }

    private String buildCacheKey(String key) {
        return "sms:" + key;
    }
}
