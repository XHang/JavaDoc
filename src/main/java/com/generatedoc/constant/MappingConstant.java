package com.generatedoc.constant;

import com.generatedoc.exception.SystemException;
import javafx.util.converter.NumberStringConverter;

import java.util.HashMap;
import java.util.Map;

public class MappingConstant {
    public static final Map<String,String> NUMBER_MAP = new HashMap<>();
    static {
        NUMBER_MAP.put("1","一");
        NUMBER_MAP.put("2","二");
        NUMBER_MAP.put("3","三");
        NUMBER_MAP.put("4","四");
        NUMBER_MAP.put("5","五");
        NUMBER_MAP.put("6","六");
        NUMBER_MAP.put("7","七");
        NUMBER_MAP.put("8","八");
        NUMBER_MAP.put("9","九");
        NUMBER_MAP.put("10","十");
    }
    public static String numberConverter(int index){
        if (index>10){
            throw new SystemException("该方法暂不支持十以上的数字-汉字转换");
        }
        return NUMBER_MAP.get(index+"");
    }
}
