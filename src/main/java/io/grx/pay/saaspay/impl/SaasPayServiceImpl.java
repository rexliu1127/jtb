package io.grx.pay.saaspay.impl;

import cn.hutool.crypto.SecureUtil;
import io.grx.common.utils.UUIDGenerator;
import io.grx.pay.saaspay.SaasPayService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaasPayServiceImpl implements SaasPayService {
    private final String apiId = "1016358";
    private final String apiKey = "e7acb782cf774081d591358c9b414005";
    private final String postUrl = "http://api.saaspay.vip/api/v1/payment";

    @Override
    public Map<String, String> payment(String title, String payType, long amount, String orderNo, String remark,
                                       String returnUrl, String notifyUrl, String feedbackUrl) {
        //- apiId, 		必填,ApiId
        // - name, 		必填, 订单销售商品名称
        // - payType, 		必填：支付方式 wechat  alipay
        // - price, 		必填：金额,格式 xx.xx 如 12.00
        // - orderNo, 		必填：订单号,要求是商户唯一订单号
        // - userinfo, 		用户备注,自定义内容
        // - returnUrl, 		支付成功返回页面
        // - notifyUrl, 		必填：支付成功通知页面
        // - feedbackUrl, 	支付超时返回页面
        // - sign, 		必填：签名
        Map<String, String> result = new HashMap<>();
        result.put("apiId", apiId);
        result.put("apiKey", apiKey);
        result.put("name", title);
        String payType2 = StringUtils.defaultIfBlank(payType, "wechat");
        result.put("payType", payType2);
        String price = String.valueOf(amount / 100.0);
        result.put("price", price);
        result.put("orderNo", orderNo);
        result.put("userinfo", remark);
        result.put("returnUrl", returnUrl);
        result.put("notifyUrl", notifyUrl);
        result.put("feedbackUrl", feedbackUrl);
        result.put("postUrl", postUrl);

        //开始验证签名
        String source = apiId+"|"+title+"|"+payType2+"|"+price+"|"+orderNo+"|"+remark+"|"+returnUrl+"|"+notifyUrl+"|"+feedbackUrl+"|"+apiKey;
        result.put("sign", SecureUtil.md5(source));
        return result;
    }
}
