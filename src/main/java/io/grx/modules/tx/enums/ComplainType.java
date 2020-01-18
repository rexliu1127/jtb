package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ComplainType implements ValueEnum {
    NO_MONEY(0, "凭证已生效对方不放款"),
    ALREADY_REPAID(1, "已按照约定还款，对方不撕毁凭证"),
    NO_CONTACT(2, "要还款联系不上对方"),
    OHTERS(3, "其他");

    private int value;
    private String displayName;

    static Map<Integer, ComplainType> enumMap = new HashMap<Integer, ComplainType>();

    static {
        for (ComplainType type : ComplainType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    ComplainType(final int value, final String displayName) {
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

    public static ComplainType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "ComplainType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
