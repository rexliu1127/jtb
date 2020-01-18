package io.grx.common.controller;

import java.util.Map;

import com.google.code.kaptcha.Constants;
import io.grx.common.utils.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.service.SmsCodeService;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.R;

@RestController
public class SmsController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private LuosimaoService luosimaoService;

    /**
     * 获取手机验证码
     */
    @RequestMapping(value = "/sms_code", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSmsCode(final String type, String mobile, String captcha) {

        if (luosimaoService.needCaptcha()) {
            if (!luosimaoService.isResponseVerified(captcha)) {
                return R.error("人机识别验证失败");
            }
        }

        return smsCodeService.getVerifyCode(mobile, type);
    }

    /**
     * 获取手机验证码
     */
    @RequestMapping(value = "/sms_code2", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSmsCode2(final String type, String mobile, String captcha) {
       /* if (StringUtils.isEmpty(ShiroUtils.getMachineId())) {
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY, false);

            if (!StringUtils.equalsIgnoreCase(kaptcha, captcha)) {
                logger.debug("captcha={}, kaptcha={}", captcha, kaptcha);
                return R.error("校验码不正确");
            }
        }*/

        return smsCodeService.getVerifyCode(mobile, type);
    }
}
