package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import io.grx.common.enums.ValueEnum;

public enum VerifyStatus implements ValueEnum {
    NEW(-1, "未提交"),
    SUBMITTED(0, "已提交"),
    SUCCESS(1, "成功"),
    FAILED(2, "认证失败"),
    PROCESSING(3, "认证中");

    private int value;
    private String displayName;

    static Map<Integer, VerifyStatus> enumMap = new HashMap<Integer, VerifyStatus>();

    static {
        for (VerifyStatus type : VerifyStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    VerifyStatus(final int value, final String displayName) {
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

    public static VerifyStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "VerifyStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
