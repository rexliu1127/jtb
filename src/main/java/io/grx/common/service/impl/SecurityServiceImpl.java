package io.grx.common.service.impl;

import io.grx.common.service.SecurityService;
import io.grx.common.utils.CharUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Value("${security.maskKeyInfo:true}")
    private boolean toMaskInfo;

    @Override
    public boolean toMaskKeyInfo() {
        return toMaskInfo;
    }

    @Override
    public String maskMiddleChars(String s, int left, int right) {
        if (!toMaskInfo) {
            return s;
        }
        return CharUtils.maskMiddleChars(s, left, right);
    }
}
