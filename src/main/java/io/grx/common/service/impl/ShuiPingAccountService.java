package io.grx.common.service.impl;

import io.grx.common.service.BankAccountService;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.Md5Utils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class ShuiPingAccountService implements BankAccountService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${bank.enabled}")
    private boolean enabled;
    @Value("${bank.fakeBankMobile}")
    private String fakeBankMobile;
    @Value("${shuiping.appId}")
    private String appId;
    @Value("${shuiping.secretKey}")
    private String secretKey;
    @Value("${shuiping.spiderUrl}")
    private String spiderUrl;
    @Value("${shuiping.dataUrl}")
    private String dataUrl;

    public static void main(String[] args) throws Exception {
        ShuiPingAccountService s = new ShuiPingAccountService();
        s.enabled = true;
        s.appId = "969043f78eabfe5f";
        s.secretKey = "";
        s.spiderUrl = "http://103.141.1.155:9626/data/api-v50/spider.aspx";
        s.dataUrl = "http://103.141.1.155:9626/data/api-v50/data.aspx";

        System.out.println(s.validateBankAccount("350583198802020113", "庄溪山", "6236681930008083832", "13860172901"));
//        s.getData("5caed1bb3a274876969fb33073d1e740");
        System.out.println();
    }

    @Override
    public String validateBankAccount(final String idNo, final String name, final String account, final String mobile) {
        if (!enabled || StringUtils.equals(mobile, fakeBankMobile)) {
            return "中国**银行";
        }

        Map<String, String> params = new TreeMap<String, String>();
        params.put("userid", appId);
        params.put("ver", "5");
        params.put("datatype", "json");
        params.put("name", name);
        params.put("phone", mobile);
        params.put("cardNo", idNo);
        params.put("bankNo", account);
        sign(params);

        params.put("api-method", "BankCard4Info");

        try {
            logger.info("check bank for params: {}", params);
            HttpResponse response = HttpUtils.doPost(spiderUrl, "", MapUtils.EMPTY_MAP, MapUtils.EMPTY_MAP, params);
            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("result: {}", responseStr);

            org.json.JSONObject responseJson = new JSONObject(responseStr);
            if (StringUtils.equalsIgnoreCase(responseJson.getString("code"), "0")) {
                String token = responseJson.getString("data");

                return getData(token);
            }
        } catch (Exception e) {
            logger.error("Failed to validate bank", e);
        }
        return null;
    }

    //get data {"code":0,"message":"success","data":"5caed1bb3a274876969fb33073d1e740"}
    private String getData(String token) {
        Map<String, String> params = new TreeMap<String, String>();

        params.put("userid", appId);
        params.put("ver", "5");
        params.put("datatype", "json");
        params.put("token", token);
        sign(params);
        try {
            HttpResponse response = HttpUtils.doPost(dataUrl, "", MapUtils.EMPTY_MAP, MapUtils.EMPTY_MAP, params);
            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("result2: {}", responseStr);

            JSONObject responseJson = new JSONObject(responseStr);
            if (StringUtils.equalsIgnoreCase(responseJson.getString("code"), "0")) {
                JSONObject data = responseJson.getJSONObject("data");

                if (StringUtils.equalsIgnoreCase(data.getString("code"), "1")) {
                    if (!data.isNull("bank_info")) {
                        JSONObject bankInfo = data.getJSONObject("bank_info");
                        return responseJson.getString("bank_name");
                    }
                    return "未知银行";
                }
            }
        } catch (Exception e) {
            logger.error("Failed to validate bank2", e);
        }
        return null;
    }

    private void sign(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append("secret=").append(secretKey);

        params.put("sign", new Md5Utils().getMD5Str(sb.toString()));
    }
}
