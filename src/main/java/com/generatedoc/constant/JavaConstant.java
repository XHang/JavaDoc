package com.generatedoc.constant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JavaConstant {
    /**
     * Java自带类型
     */
    public static final List<String> OWN_TYPE = new ArrayList<>();


    public static  final String STRING_TPYE = "String";

    public static final List<String> NUMBER_TYPES = new ArrayList<>();

    public static final List<String> COLLECTION_TYPES = new ArrayList<>();

    static {
        COLLECTION_TYPES.add("List");
        COLLECTION_TYPES.add("Set");
        COLLECTION_TYPES.add("ArrayList");
    }

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
        OWN_TYPE.addAll(COLLECTION_TYPES);
        OWN_TYPE.addAll(NUMBER_TYPES);
        OWN_TYPE.add("Object");
        OWN_TYPE.add("Date");
        OWN_TYPE.add("Timestamp");
        OWN_TYPE.add("LocalDate");
    }
    public static final String INTEGER_MIN_NAME = "Integer.MIN_VALUE";
    public static final String INTEGER_MAX_NAME =  "Integer.MAX_VALUE";

    public static final String INTEGER_MIN_VALUE = "0x80000000";
    public static final String Integer_MAX_VALUE = "0x7fffffff";

}
