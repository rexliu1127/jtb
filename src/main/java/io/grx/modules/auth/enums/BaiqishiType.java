package io.grx.modules.auth.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BaiqishiType implements ValueEnum {
    MNO(0, "手机运营商"),
    CHSI(1, "学信网 "),
    MAIMAI(2, "脉脉"),
    JD(3, "京东"),
    TB(4, "淘宝"),
    ALIPAY(5, "支付宝"),
    DIDI(6, "滴滴"),
    ZM(7, "芝麻分");

    private int value;
    private String displayName;

    static Map<Integer, BaiqishiType> enumMap = new HashMap<Integer, BaiqishiType>();

    static {
        for (BaiqishiType type : BaiqishiType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    BaiqishiType(final int value, final String displayName) {
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

    public static BaiqishiType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "BaiqishiType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
