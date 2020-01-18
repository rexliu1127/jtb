package io.grx.pay.service;

import java.util.Map;

public interface AllinPayService {

    Map<String, String> payByWx(long amount, String orderNo,
                                String title,
                                String remark, String openId,
                                String callbackUrl) throws Exception;
}
