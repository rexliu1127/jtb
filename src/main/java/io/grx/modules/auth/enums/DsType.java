package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import io.grx.common.enums.ValueEnum;

public enum DsType implements ValueEnum {
    JD(0, "京东"),
    SN(1, "苏宁");

    private int value;
    private String displayName;

    static Map<Integer, DsType> enumMap = new HashMap<Integer, DsType>();

    static {
        for (DsType type : DsType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    DsType(final int value, final String displayName) {
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

    public static DsType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "DsType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
