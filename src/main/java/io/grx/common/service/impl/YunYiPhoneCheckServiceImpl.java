package io.grx.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grx.common.service.PhoneCheckService;
import io.grx.common.utils.HttpUtils;

@Service
public class YunYiPhoneCheckServiceImpl implements PhoneCheckService {

    @Value("${phoneCheck.yunyi.host}")
    private String host;
    @Value("${phoneCheck.yunyi.path}")
    private String path;
    @Value("${phoneCheck.yunyi.appcode}")
    private String appcode;
    @Value("${phoneCheck.enabled}")
    private boolean enabled;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int RETRY_COUNT = 3;

    public static void main(String[] args) {
        String host = "https://phonecheck.market.alicloudapi.com";
        String path = "/phoneAuthentication";
        String method = "POST";
        String appcode = "c14eecfc5a0f4e4a9eca40cd876ef1a4";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("idNo", "");
        bodys.put("name", "杨志辉");
        bodys.put("phoneNo", "");

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
//            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPhoneVerified(final String name, final String idNo, final String phone) {
        if (!enabled) {
            return true;
        }

        int retryCount = 0;
        do {
            try {
                Map<String, String> headers = new HashMap<String, String>();
                //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
                headers.put("Authorization", "APPCODE " + appcode);
                //根据API的要求，定义相对应的Content-Type
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                Map<String, String> querys = new HashMap<String, String>();
                Map<String, String> bodys = new HashMap<String, String>();
                bodys.put("idNo", idNo);
                bodys.put("name", name);
                bodys.put("phoneNo", phone);

                logger.info("going to phone check for {},{},{}", name, idNo, phone);
                HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
                String responseStr = EntityUtils.toString(response.getEntity());
                logger.info("result: {}", responseStr);
                JSONObject responseJson = new JSONObject(responseStr);

                return StringUtils.equalsIgnoreCase(responseJson.getString("respCode"), "0000");
            } catch (Throwable t) {
                logger.error("Failed to phone check", t);

                retryCount++;
            }
        } while (retryCount < RETRY_COUNT);

        return false;
    }
}
