package com.generatedoc.constant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JavaConstant {
    /**
     * Java自带类型
     */
    public static final List<String> OWN_TYPE = new ArrayList<>();

    public static  final String LIST_TPYE = "List";

    public static  final String STRING_TPYE = "String";

    public static final List<String> NUMBER_TYPES = new ArrayList<>();

    static {
        NUMBER_TYPES.add("Integer");
        NUMBER_TYPES.add("Double");
        NUMBER_TYPES.add("Long");
        NUMBER_TYPES.add("int");
        NUMBER_TYPES.add("long");
        NUMBER_TYPES.add("double");
        NUMBER_TYPES.add("BigDecimal");
    }
    static{
        OWN_TYPE.add(STRING_TPYE);
        OWN_TYPE.add("Boolean");
        OWN_TYPE.add("Map");
        OWN_TYPE.add(LIST_TPYE);
        OWN_TYPE.addAll(NUMBER_TYPES);
    }
}
