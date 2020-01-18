package io.grx.modules.auth.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MobileReNameCheck {
    Features_0("1","完全一致"),
    Features_2("2","基本一致"),
    Features_5("3","不一致"),
    Features_6("4","无法验证"),
    Features_7("5","无法验证");


    public final String code;
    public final String value;

    public static final Map<String, MobileReNameCheck>   etype = new HashMap<String, MobileReNameCheck>();
    static{
        for(MobileReNameCheck type :EnumSet.allOf(MobileReNameCheck.class))
        {
            etype.put(type.getCode(), type);
        }

    }
    MobileReNameCheck(String code, String value)
    {
        this.code = code;
        this.value = value;
    }

    public static MobileReNameCheck get(String code)
    {
        return etype.get(code);
    }

    public String getCode()
    {
        return code;
    }

    public String getValue()
    {
        return value;
    }

}
