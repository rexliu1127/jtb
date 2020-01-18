package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UsageType implements ValueEnum {
    OTHER(0, "其他"),
    INDIVIDUAL(1, "个体经营"),
    EXPENSE(2, "消费"),
    LEARN_SUPPORT(3, "助学"),
    STARTUP(4, "创业"),
    LENT(5, "租房"),
    TRIP(6, "旅游"),
    DECORATE(7, "装修"),
    HEATH(8, "医疗");

    private int value;
    private String displayName;

    static Map<Integer, UsageType> enumMap = new HashMap<Integer, UsageType>();

    static {
        for (UsageType type : UsageType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    UsageType(final int value, final String displayName) {
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

    public static UsageType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "UsageType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
