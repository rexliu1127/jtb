package io.grx.common.service.impl;

import io.grx.common.service.SmsService;
import io.grx.common.utils.HttpUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service("cl253SmsService")
public class CL253SmsService implements SmsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Environment env;

    @Value("${sms.cl253.url}")
    private String url;
    @Value("${sms.cl253.account}")
    private String account;
    @Value("${sms.cl253.password}")
    private String password;

    @Override
    public boolean sendVerifyCode(String mobile, String type, String code, Map<String, String> params) {
        String msg = String.format("【友信用中心】您好！验证码是：%1$s，短信有效时间为5分钟。", code);
        Map<String, Object> bodys = new LinkedHashMap<>();

        bodys.put("account", account);
        bodys.put("password", password);
        bodys.put("msg", msg);
        bodys.put("phone", mobile);

        logger.info("Sending SMS code. params: {}", bodys);
        try {
//            HttpResponse httpResponse = HttpUtils.doPost(url, MapUtils.EMPTY_MAP, bodys);
            Map<String, Object> responseMap = HttpUtils.postJsonData(url, "", MapUtils.EMPTY_MAP, bodys);
            logger.info("result: {}", responseMap);

            int status = MapUtils.getIntValue(responseMap, "code", -1);

            return status == 0;
        } catch (Exception e) {
            logger.error("failed to send sms", e);
        }

        return false;
    }

    public static void main(String[] args) {
//        CL253SmsService service = new CL253SmsService();
//        service.url = "http://120.79.207.210:7862/sms";
//        service.account = "888008";
//        service.password = "SQdbtBaaaa";
//        service.extNo = "10690008";
//        service.sign = "【米兜凭证】";
//
//        boolean result = service.sendVerifyCode("15018965933", "login", "123456", new HashMap<>());
//        System.out.println(result);

        String url = "http://smssh1.253.com/msg/send/json";

        Map<String, Object> bodys = new HashMap<>();
        bodys.put("account", "N2307010");
        bodys.put("password", "wNTrG7bnHCd425");
        bodys.put("msg", "【友信用中心】您好！验证码是：%1$s，短信有效时间为5分钟。");
        bodys.put("phone", "19925332372");

        try {
//            HttpResponse httpResponse = HttpUtils.doPost(url, MapUtils.EMPTY_MAP, bodys);
            Map<String, Object> responseMap = HttpUtils.postJsonData(url, "", MapUtils.EMPTY_MAP, bodys);
//            HttpResponse httpResponse = HttpUtils.doGet(url, "", MapUtils.EMPTY_MAP, bodys);
//            String response = EntityUtils.toString(httpResponse.getEntity(), Constant.ENCODING_UTF8);
            System.out.println("result: " + responseMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
