package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import io.grx.common.enums.ValueEnum;

public enum AuthVendorType implements ValueEnum {
    SJMH(0, "数据魔盒"),
    JXL(1, "聚信立"),
    TJ(2, "天机");

    private int value;
    private String displayName;

    static Map<Integer, AuthVendorType> enumMap = new HashMap<Integer, AuthVendorType>();

    static {
        for (AuthVendorType type : AuthVendorType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    AuthVendorType(final int value, final String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static AuthVendorType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "AuthVendorType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
