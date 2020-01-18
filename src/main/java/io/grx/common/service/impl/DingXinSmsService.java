package io.grx.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import io.grx.common.service.SmsService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;
import org.springframework.stereotype.Service;


@Service("dingXinSmsService")
public class DingXinSmsService implements SmsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Value("${sms.dingxin.host}")
    private String host;
    @Value("${sms.dingxin.path}")
    private String path;
    @Value("${sms.dingxin.appcode}")
    private String appcode;
    @Value("${sms.dingxin.defaultVerifyCodeId}")
    private String defaultVerifyCodeId;

    @Override
    public boolean sendVerifyCode(final String mobile, final String type, final String code, final Map<String, String> params) {
        String templateId = env.getProperty(String.format("sms.dingxin.%1$sVerifyCodeId", type), defaultVerifyCodeId);
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);

        String param = "code:" + code;
        if (params != null) {
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    param = param + "," + entry.getKey() + ":" + entry.getValue();
                }
//                param = URLEncoder.encode(param, Constant.ENCODING_UTF8);
            } catch (Exception e) {
                logger.error("Cannot encode sms parameters", e);
            }
        }
        querys.put("param", param);
        querys.put("tpl_id", templateId);
        Map<String, String> bodys = new HashMap<String, String>();

        logger.info("Sending {} SMS code {} to {}, with template ID {} and params: {}", type, code, mobile,
                templateId, param);
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
            String responseStr = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("result: {}", responseStr);

            JSONObject responseJson = new JSONObject(responseStr);
            if (StringUtils.equalsIgnoreCase(responseJson.getString("return_code"), "00000")) {
                return true;
            }
        } catch (Exception e) {
            logger.error("failed to send sms", e);
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String appcode = "";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15018965933");
        querys.put("param", "code:123401");
        querys.put("tpl_id", "TP1712284");
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
            System.out.println("response: " + EntityUtils.toString(response.getEntity()));
            //获取response的body
//            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
