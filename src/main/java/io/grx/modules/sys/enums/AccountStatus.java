package io.grx.modules.sys.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.grx.common.enums.ValueEnum;

public enum AccountStatus implements ValueEnum {
    DISABLED(0, "已禁用"),
    ENABLED(1, "正常");

    private int value;
    private String displayName;

    static Map<Integer, AccountStatus> enumMap = new HashMap<Integer, AccountStatus>();

    static {
        for (AccountStatus type : AccountStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    AccountStatus(final int value, final String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static AccountStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "AccountStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
