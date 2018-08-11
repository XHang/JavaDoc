package com.generatedoc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 提供一个文件名，获取这个文件的类型。
     * 即是获取后缀名
     * @param fileName
     */
    public static String getFileNameSuffix(String fileName){
        Pattern reg=Pattern.compile("\\.\\w{3,4}$");
        Matcher p=reg.matcher(fileName);
        p.find();
        return p.group();
    }
    /**
     * 提供一个文件路径。帮你提取出文件路径中的文件名称
     * @param path
     * @return
     */
    public static String getFileNameByPath(String path){
        Pattern reg=Pattern.compile("\\w+\\.\\w+$");
        Matcher p=reg.matcher(path);
        p.find();
        return p.group();
    }
    public static boolean isEmpty(String str){
        return (str == null || str.trim().length() ==0);
    }
    public static boolean isNoneEmpty(String str){
        return !isEmpty(str);
    }
}
