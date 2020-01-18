package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ComplainResult implements ValueEnum {
    PENDING(0, "未处理"),
    PROCESSING(1, "处理中"),
    COMPLETED(2, "已处理");

    private int value;
    private String displayName;

    static Map<Integer, ComplainResult> enumMap = new HashMap<Integer, ComplainResult>();

    static {
        for (ComplainResult type : ComplainResult.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    ComplainResult(final int value, final String displayName) {
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

    public static ComplainResult valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "ComplainResult{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}