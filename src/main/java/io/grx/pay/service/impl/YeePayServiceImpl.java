package io.grx.pay.service.impl;

import com.google.gson.Gson;
import io.grx.pay.service.YeePayService;
import io.grx.pay.yeepay.YeepayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class YeePayServiceImpl implements YeePayService {
    @Value("${wechat.appId}")
    private String appId;

    @Override
    public Map<String, String> payByWx(long amount, String orderNo, String title, String remark,
                                       String openId, String returnUrl, String callbackUrl) throws Exception {
        String timeoutExpress = "";
        String requestDate = "";
        String paymentParamExt = "";
        String memo = "";
        String riskParamExt = "";
        String csUrl = "";

        String goodsParamExt = "{\"goodsName\":\""+title+"\",\"goodsDesc\":\""+title+"\"}";
        String industryParamExt = "";

        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderNo);
        params.put("orderAmount", String.valueOf(amount/ 100.0));
        params.put("timeoutExpress", timeoutExpress);
        params.put("requestDate", requestDate);
        params.put("redirectUrl", returnUrl);
        params.put("notifyUrl", callbackUrl);
        params.put("goodsParamExt", goodsParamExt);
        params.put("paymentParamExt", paymentParamExt);
        params.put("industryParamExt", industryParamExt);
        params.put("memo", memo);
        params.put("riskParamExt", riskParamExt);
        params.put("csUrl", csUrl);

        Map<String, String> result = new HashMap<>();
        String uri = YeepayService.getUrl(YeepayService.TRADEORDER_URL);
        result = YeepayService.requestYOP(params, uri, YeepayService.TRADEORDER);

        String token = result.get("token");
        String codeRe = result.get("code");
        if(!"OPR00000".equals(codeRe)){
            return result;
        }

        params = new HashMap<>();

        params.put("payTool", "WECHAT_OPENID");
        params.put("payType", "WECHAT");
//        params.put("orderId", orderId);
        params.put("token", token);
        params.put("appId", appId);
        params.put("openId", openId);
        params.put("version", "1.0");
        params.put("userIp", "0.0.0.0");
        params.put("extParamMap", "{\"reportFee\":\"XIANSHANG\"}");

        result = new HashMap<>();
        String url = YeepayService.getUrl(YeepayService.APICASHIER_URI);

        System.out.println(new Gson().toJson(params));

        result = YeepayService.requestYOP(params, url, YeepayService.APICASHIER);
        return result;
    }
}
