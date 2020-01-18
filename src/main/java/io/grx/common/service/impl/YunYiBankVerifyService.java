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

import io.grx.common.service.BankAccountService;
import io.grx.common.utils.HttpUtils;
import org.springframework.stereotype.Service;

//@Service
public class YunYiBankVerifyService implements BankAccountService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${bank.yunyi.host}")
    private String host;
    @Value("${bank.yunyi.path}")
    private String path;
    @Value("${bank.yunyi.appcode}")
    private String appcode;
    @Value("${bank.enabled}")
    private boolean enabled;
    @Value("${bank.fakeBankMobile}")
    private String fakeBankMobile;

    @Override
    public String validateBankAccount(final String idNo, String name, final String account, final String mobile) {
        if (!enabled || StringUtils.equals(mobile, fakeBankMobile)) {
            return "中国**银行";
        }
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("ReturnBankInfo", "YES");
        bodys.put("cardNo", account);
        bodys.put("idNo", idNo);
        bodys.put("name", name);
        bodys.put("phoneNo", mobile);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("result: {}", responseStr);

            JSONObject responseJson = new JSONObject(responseStr);
            if (StringUtils.equalsIgnoreCase(responseJson.getString("respCode"), "0000")) {
                return responseJson.getString("bankName");
            }
        } catch (Exception e) {
            logger.warn("Error in validating bank account", e);
        }

        return null;
    }

    public static void main(String[] args) {
        String host = "https://yunyidata.market.alicloudapi.com";
        String path = "/bankAuthenticate4";
        String method = "POST";
        String appcode = "";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("ReturnBankInfo", "YES");
        bodys.put("cardNo", "");
        bodys.put("idNo", "");
        bodys.put("name", "");
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
            System.out.println("response: " + EntityUtils.toString(response.getEntity()));
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
