package com.generatedoc.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static Logger logger = LogManager.getLogger(FileUtil.class);


    public static boolean  isEmpty(String str){
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0){
            return true;
        }
       if ("null".equals(str) || "NULL".equals(str)){
            return true;
       }
       return false;
    }

    /**
     * exportPath
     * @param fullName 类的完全限定名，例如为com.cxh.doc.AC
     * @return 类的简称，例如AC
     */
    public static String getClassNameForFullName(String fullName){
        if (isEmpty(fullName)){
            return fullName;
        }
        //匹配.  字母若干  结尾。
        //丢弃.符号
        String reg = "(?<=\\.)\\w+$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(fullName);
        Boolean isMtcher = matcher.find();
        if (!isMtcher){
            return fullName;
        }else{
            return matcher.group();
        }
    }

    public static String removeBothSideChar(String src){
        if (StringUtil.isEmpty(src)){
            return "";
        }
        return src.substring(1,src.length()-1);
    }

    /**
     * 检测给定的字符串，首字母是否大写
     * @param str
     * @return
     */
    public static final boolean isFirstUpperCase(String str){
        char first = str.charAt(0);
        if (first<'a'){
            return true;
        }
        return false;
    }
}
