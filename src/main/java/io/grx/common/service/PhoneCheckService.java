package io.grx.common.service;

public interface PhoneCheckService {
    boolean isPhoneVerified(String name, String idNo, String phone);
}
