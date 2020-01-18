package io.grx.common.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.grx.common.service.SmsService;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.UUIDGenerator;

@Service("dh3tSmsService")
public class Dh3tSmsService implements SmsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Value("${sms.dh3t.host}")
    private String host;
    @Value("${sms.dh3t.path}")
    private String path;
    @Value("${sms.dh3t.account}")
    private String account;
    @Value("${sms.dh3t.password}")
    private String password;
    @Value("${sms.dh3t.sign}")
    private String sign;

    @Override
    public boolean sendVerifyCode(final String mobile, final String type, final String code, final Map<String, String> params) {
        String label = "";
        if (env != null) {
            label = StringUtils.defaultIfBlank(env.getProperty(String.format("sms.dh3t.%1$sLabel", type)), "");
        }

        String msg = String.format("尊敬的用户，您本次%1$s操作的短信验证码：%2$s。切勿告知他人！", label, code, sign);
        Map<String, Object> bodys = new LinkedHashMap<>();

        bodys.put("account", account);
        bodys.put("password", password);
        bodys.put("msgid", UUIDGenerator.getUUID());
        bodys.put("phones", mobile);
        bodys.put("content", msg);
        bodys.put("sign", sign);
        bodys.put("subcode", "");
        bodys.put("sendtime", "");

        logger.info("Sending SMS code. params: {}", bodys);
        try {
            Map<String, Object> response = HttpUtils.postJsonData(host, path, MapUtils.EMPTY_MAP, bodys);
            logger.info("result: {}", response);

            if (MapUtils.getIntValue(response, "result", -1) == 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error("failed to send sms", e);
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        /*
        {"account":"dh14481","password":"09c4c9f585ae145e83759d0ca50128b3","msgid":"2c92825934837c4d0134837dcba00151","phones":"13631235365","content":"您好，您的手机验证码为：430237。","sign":"【存信宝】","subcode":"","sendtime":""}
         */
        Dh3tSmsService service = new Dh3tSmsService();
        service.account = "dh144";
        service.password = "09c4c9f585ae145e83759d0ca50128b3";
        service.sign = "【存信宝】";
        service.host = "http://www.dh3t.com";
        service.path = "/json/sms/BatchSubmit";
        service.sendVerifyCode("13631235365", "login", "123456", null);
    }
}
