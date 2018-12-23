package com.generatedoc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUitl {

     public static final String CHINA_FORMAT = "yyyy年MM月dd日  HH小时MM分钟SS秒";

    /**
     * 格式化日期
     */
    public static String formatterChinaDate(LocalDateTime localDateTime){
       return formatterDate(localDateTime, CHINA_FORMAT);
    }
    public static String formatterDate(LocalDateTime localDateTime,String foramt){
        return localDateTime.format(DateTimeFormatter.ofPattern(foramt));
    }
}
