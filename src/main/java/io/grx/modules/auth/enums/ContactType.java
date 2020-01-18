package io.grx.modules.auth.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.grx.common.enums.ValueEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContactType implements ValueEnum {
    SPOUSE(0, "配偶"),
    PARENT(1, "父母"),
    RELATIVE(2, "兄弟姐妹");

    private int value;
    private String displayName;

    static Map<Integer, ContactType> enumMap = new HashMap<Integer, ContactType>();

    static {
        for (ContactType type : ContactType.values()) {
            enumMap.put(type.getValue(), type);
        }
    }

    ContactType(final int value, final String displayName) {
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

    public static ContactType valueOf(int value) {
        return enumMap.get(value);
    }

    @Override
    public String toString() {
        return "ContactType{" +
                "value=" + value +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
