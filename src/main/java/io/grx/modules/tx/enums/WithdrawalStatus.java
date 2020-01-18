package io.grx.modules.tx.enums;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WithdrawalStatus implements ValueEnum {
    UNKNOWN(-1, "未知"),
    NEW(1, "待审核"),
    APPROVED(2, "已审核"),
    REJECTED(3, "已拒绝"),
    COMPLETED(4, "已放款"),
    PROCESSING(5, "提现中"),
    FAILED(6, "提现失败");

    private int value;
    private String displayName;

    static Map<Integer, WithdrawalStatus> enumMap = new HashMap<Integer, WithdrawalStatus>();

    static {
        for (WithdrawalStatus type : WithdrawalStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    WithdrawalStatus(final int value, final String displayName) {
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

    public static WithdrawalStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "WithdrawalStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
