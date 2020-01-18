package io.grx.common.service;


import java.util.Map;

public interface SmsService {
    boolean sendVerifyCode(String mobile, String type, String code, Map<String, String> params);
}
