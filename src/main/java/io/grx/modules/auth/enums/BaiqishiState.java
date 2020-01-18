package io.grx.modules.auth.enums;

import io.grx.common.enums.ValueEnum;

import java.util.HashMap;
import java.util.Map;

public enum BaiqishiState implements ValueEnum {
    INIT(0, "初始化"),
    LOGIN(1, "登录成功"),
    CRAWL(2, "数据成功"),
    CRAWL_FAIL(3, "数据失败"),
    REPORT(4, "生成报告成功"),
    REPORT_FAIL(5, "生成报告失败");

    private int value;
    private String displayName;

    static Map<Integer, BaiqishiState> enumMap = new HashMap<Integer, BaiqishiState>();

    static {
        for (BaiqishiState type : BaiqishiState.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    BaiqishiState(final int value, final String displayName) {
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

    public static BaiqishiState valueOf(int value) {
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
