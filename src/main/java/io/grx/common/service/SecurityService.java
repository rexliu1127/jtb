package io.grx.common.service;

public interface SecurityService {
    boolean toMaskKeyInfo();

    String maskMiddleChars(String s, int left, int right);
}
