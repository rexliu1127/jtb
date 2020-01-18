package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RepaymentStatus implements ValueEnum {
    UNKNOWN(-1, "未知"),
    NEW(1, "待确认"),
    CONFIRMED(2, "已确认"),
    CANCELLED(3, "已取消"),
    REJECTED(4, "已拒绝");

    private int value;
    private String displayName;

    static Map<Integer, RepaymentStatus> enumMap = new HashMap<Integer, RepaymentStatus>();

    static {
        for (RepaymentStatus type : RepaymentStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    RepaymentStatus(final int value, final String displayName) {
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

    public static RepaymentStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "RepaymentStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
