package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExtensionStatus implements ValueEnum {
    UNKNOWN(-1, "未知"),
    NEW(1, "h"),
    CONFIRMED(2, "已确认"),
    CANCELLED(3, "已取消"),
    REJECTED(4, "已拒绝"),
    UNPAID(5, "待支付");

    private int value;
    private String displayName;

    static Map<Integer, ExtensionStatus> enumMap = new HashMap<Integer, ExtensionStatus>();

    static {
        for (ExtensionStatus type : ExtensionStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    ExtensionStatus(final int value, final String displayName) {
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

    public static ExtensionStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "ExtensionStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
