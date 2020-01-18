package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TianjiType implements ValueEnum {
    MOBILE(0, "运营商"),
    JD(1, "京东"),
    ALIPAY(2, "支付宝"),
    CREDIT(3, "网银信用卡报告"),
    CREDIT_EMAIL(4, "邮箱信用卡报告"),
    TAOBAO_CRAWL(5, "淘宝"),
    INSURE(6, "社保"),
    FUND(7, "公积金"),
    EMAIL_CRAWL(8, "邮箱");

    private int value;
    private String displayName;

    static Map<Integer, TianjiType> enumMap = new HashMap<Integer, TianjiType>();

    static {
        for (TianjiType type : TianjiType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    TianjiType(final int value, final String displayName) {
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

    public static TianjiType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "TianjiType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
