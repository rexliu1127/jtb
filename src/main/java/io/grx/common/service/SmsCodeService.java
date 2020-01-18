package io.grx.common.service;

import io.grx.common.utils.R;

public interface SmsCodeService {
    R getVerifyCode(String mobile, String type);

    boolean isVerifyCodeMatch(String mobile, String type, String code);
}
