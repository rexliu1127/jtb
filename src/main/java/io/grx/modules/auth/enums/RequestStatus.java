package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequestStatus implements ValueEnum {
    NEW(-1, "未提交"),
    PENDING(0, "待审核"),
    PROCESSING(1, "审核中"),
    APPROVED(2, "已放款"),
    REJECTED(3, "拒绝受理"),
    COMPLETED(4, "已结清"),
    OVERDUE(5, "逾期"),
    LOST(6, "失联"),
    CANCELED(7, "已取消");

    private int value;
    private String displayName;

    static Map<Integer, RequestStatus> enumMap = new HashMap<Integer, RequestStatus>();

    static {
        for (RequestStatus type : RequestStatus.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    RequestStatus(final int value, final String displayName) {
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

    public static RequestStatus valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "RequestStatus{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
