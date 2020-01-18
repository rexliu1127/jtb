package io.grx.modules.pay.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayStatus implements ValueEnum {
    NEW(0, "待确认"),
    COMPLETE(1, "已完成"),
    CANCELLED(2, "已取消"),
    ERROR(3, "支付出错");

    private int value;
    private String displayName;

    static Map<Integer, PayStatus> enumMap = new HashMap<Integer, PayStatus>();

    static {
        for (PayStatus type : PayStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    PayStatus(final int value, final String displayName) {
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

    public static PayStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "PayStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
