package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import io.grx.common.enums.ValueEnum;

public enum YiXiangType implements ValueEnum {
    JINJIEDAO(0, "今借到"),
    WUYOUJIETIAO(1, "无忧借条"),
    MIFANG(2, "米房"),
    JIEDAIBAO(3, "借贷宝"),
    TAOBAOPAY(4, "淘宝支付宝聚合"),
    YOUPINZHENG(5, "有凭证"),
    QQPIM(6, "QQ通讯录同步"),
    BAIDUYUN(7, "百度云通讯录"),
    HULUOBO(8, "借贷报告");  //灯塔供应商提供的胡萝卜接口可直接获取无忧，今借到  借贷宝

    private int value;
    private String displayName;

    static Map<Integer, YiXiangType> enumMap = new HashMap<Integer, YiXiangType>();

    static {
        for (YiXiangType type : YiXiangType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    YiXiangType(final int value, final String displayName) {
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

    public static YiXiangType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "YiXiangType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
