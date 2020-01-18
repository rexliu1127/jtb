package io.grx.modules.auth.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  StaffType implements ValueEnum {
    WX(0, "微信客服"),
    QQ(1, "QQ客服");

    private int value;
    private String displayName;

    static Map<Integer, StaffType> enumMap = new HashMap<Integer, StaffType>();

    static {
        for (StaffType type : StaffType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    StaffType(final int value, final String displayName) {
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

    public static StaffType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "StaffType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
