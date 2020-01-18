package io.grx.modules.pay.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayScanStatus implements ValueEnum {
    PROCESSING(0, "支付中"),
    COMPLETE(1, "已完成"),
    FAILED(2, "失败");

    private int value;
    private String displayName;

    static Map<Integer, PayScanStatus> enumMap = new HashMap<Integer, PayScanStatus>();

    static {
        for (PayScanStatus type : PayScanStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    PayScanStatus(final int value, final String displayName) {
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

    public static PayScanStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "PayScanStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
