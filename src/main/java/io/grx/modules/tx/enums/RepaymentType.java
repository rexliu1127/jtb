package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RepaymentType implements ValueEnum {
    UNKNOWN(-1, "未知"),
    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信"),
    BANK(3, "银行"),
    CASH(4, "现金");

    private int value;
    private String displayName;

    static Map<Integer, RepaymentType> enumMap = new HashMap<Integer, RepaymentType>();

    static {
        for (RepaymentType type : RepaymentType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    RepaymentType(final int value, final String displayName) {
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


    public static RepaymentType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "RepaymentType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
