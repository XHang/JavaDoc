package com.generatedoc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUitl {

    /**
     * 格式化日期
     */
    public static String formatterDate(LocalDateTime localDateTime){
       return localDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日  HH小时MM分钟SS秒"));
    }
}
