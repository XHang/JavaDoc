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
    public static final Map<String,String> BIT_WORK = new HashMap<>();
    static{
        BIT_WORK.put("1","个");
        BIT_WORK.put("2","十");
        BIT_WORK.put("3","百");
        BIT_WORK.put("4","千");
    }

    /**
     * 先定个小目标，比如说，先转换它一个亿
     * @param number
     * @return
     */
    public static String numberConverter(int number){
        char [] bit = (number+"").toCharArray();
        if (bit.length>4){
            throw new SystemException("不支持对千位以上的数字进行转换");
        }
        StringBuilder sb = new StringBuilder();
        int bitIndex = 1;
        for (int index = bit.length-1;index>=0;index--){
            String bitValue = bit[index]+"";
            if (bitIndex == 1){
                sb.append(NUMBER_MAP.get(bitValue));
            }
            else{
                StringBuilder stringBuilder = new StringBuilder(BIT_WORK.get(bitIndex+""));
                sb.append(stringBuilder.reverse()+NUMBER_MAP.get(bitValue));
            }
            bitIndex++;
        }
       return sb.reverse().toString();
    }
}
