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
            if(annotationName.equals(annotation.getType().getName())){
                return annotation;
            }
        }
        return null;
    }
}
