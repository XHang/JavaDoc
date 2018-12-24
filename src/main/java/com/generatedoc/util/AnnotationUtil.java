package com.generatedoc.util;

import com.thoughtworks.qdox.model.JavaAnnotation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AnnotationUtil {

     public static final Logger log = LoggerFactory.getLogger(AnnotationUtil.class);
    /**
     * 是否存在指定的注解
     * @param annotationName
     * @param annotations
     * @return
     */
    public static boolean isExistAnnotation(String annotationName, List<JavaAnnotation> annotations){
        JavaAnnotation annotation = getAnnotationByName(annotationName,annotations);
        if (annotation == null) {
            return false;
        }
        return true;
    }
    public static JavaAnnotation getAnnotationByName(String annotationName,List<JavaAnnotation> annotations){
        if (CollectionUtils.isEmpty(annotations)){
            return null;
        }
        for (JavaAnnotation annotation : annotations) {
            String actualName = getSimpleClassName(annotation.getType().getName());
            if(annotationName.equals(actualName)){
                return annotation;
            }
        }
        return null;
    }
    public static String getSimpleClassName(String className){
        return getLastStrWithoutDot(className);
    }
    public static String getLastStrWithoutDot(String className){
        if (StringUtil.isEmpty(className)){
            return "";
        }
        int index =  className.lastIndexOf(".");
        if (index == -1){
            return className;
        }
        String result =  className.substring(index+1,className.length());
        return result;
    }
}
