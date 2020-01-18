package io.grx.modules.auth.enums;

import io.grx.common.enums.ValueEnum;

import java.util.HashMap;
import java.util.Map;

public enum CreditType implements ValueEnum {
    BASIC(0, "基本资料"),
    RELATION(1, "紧急联系人"),
    MOBILE(2, "运营商"),
    CONTACT(3, "通讯录"),
    ALIPAY(4, "支付宝"),
    TAOBAO(5, "淘宝"),
    JINGDONG(6, "京东"),
    JINJIEDAO(7, "今借到"),
    WUYOUJIETIAO(8, "无忧借条"),
    MIFANG(9, "米房"),
    JIEDAIBAO(10, "借贷宝"),
    YOUPINZHENG(11, "有凭证"),
    INSURE(12, "社保"),
    FUND(13, "公积金"),
    MNO(14, "手机运营商");

    private int value;
    private String displayName;

    static Map<Integer, CreditType> enumMap = new HashMap<Integer, CreditType>();

    static {
        for (CreditType type : CreditType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    CreditType(final int value, final String displayName) {
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

    public static CreditType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "CreditType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}

