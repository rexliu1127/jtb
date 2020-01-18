package io.grx.pay.saaspay;

import java.util.Map;

public interface SaasPayService {
    Map<String, String> payment(String title, String payType, long amount,
                                String orderNo, String remark, String returnUrl,
                                String notifyUrl, String feedbackUrl);
}
