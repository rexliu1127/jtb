package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import io.grx.common.enums.ValueEnum;

public enum TianjiState implements ValueEnum {
    INIT(0, "初始化"),
    LOGIN(1, "登录成功"),
    CRAWL(2, "数据成功"),
    CRAWL_FAIL(3, "数据失败"),
    REPORT(4, "生成报告成功"),
    REPORT_FAIL(5, "生成报告失败");

    private int value;
    private String displayName;

    static Map<Integer, TianjiState> enumMap = new HashMap<Integer, TianjiState>();

    static {
        for (TianjiState type : TianjiState.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    TianjiState(final int value, final String displayName) {
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

    public static TianjiState valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "TianjiState{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
