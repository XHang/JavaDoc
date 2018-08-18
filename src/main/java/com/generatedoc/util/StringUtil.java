package com.generatedoc.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static Logger logger = LogManager.getLogger(FileUtil.class);


    public static boolean  isEmpty(String str){
        return str== null || str.trim().length() == 0;
    }

    /**
     * exportPath
     * @param fullName 类的完全限定名，例如为com.cxh.doc.AC
     * @return 类的简称，例如AC
     */
    public static String getClassNameForFullName(String fullName){
        logger.trace("接受到的类的完全限定名为：【"+fullName+"】");
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
}
