package io.grx.modules.tx.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TxStatus implements ValueEnum {
    UNKNOWN(-1, "未知"),
    NEW(1, "待确定"),
    CONFIRMED(2, "还款中"),
    COMPLETED(3, "已还清"),
    DELAYED(4, "已逾期"),
    UNPAID(5, "未支付"),
    REJECTED(6, "已驳回");

    private int value;
    private String displayName;

    static Map<Integer, TxStatus> enumMap = new HashMap<Integer, TxStatus>();

    static {
        for (TxStatus type : TxStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    TxStatus(final int value, final String displayName) {
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

    public static TxStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "TxStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
