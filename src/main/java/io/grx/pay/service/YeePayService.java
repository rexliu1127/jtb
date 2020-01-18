package io.grx.pay.service;

import java.util.Map;

public interface YeePayService {

    Map<String, String> payByWx(long amount, String orderNo,
                                String title,
                                String remark, String openId,
                                String returnUrl,
                                String callbackUrl) throws Exception;
}
