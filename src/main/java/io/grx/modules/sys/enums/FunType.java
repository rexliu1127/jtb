package io.grx.modules.sys.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

import java.util.HashMap;
import java.util.Map;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FunType implements ValueEnum {
    DUOTOU(1, "多头报告"),
    ZHIFUBAO(2, "支付宝报告"),
	BUYFLOW(3, "流量采购");

    private int value;
    private String displayName;

    static Map<Integer, FunType> enumMap = new HashMap<Integer, FunType>();

    static {
        for (FunType type : FunType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    FunType(final int value, final String displayName) {
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

    public static FunType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "FunType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
