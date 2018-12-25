package com.generatedoc.context;


import com.thoughtworks.qdox.model.JavaClass;

import java.util.HashMap;
import java.util.Map;

/**
 *接口文档生成器的Java包上下文，主要是将遍历拿到的类和包Duang到这里面
 */
public class ClassContext {
    /**
     * 存放扫描到的所有java类
     * key是类的全程，valu是JavaClass
     */
    private static Map<String,JavaClass> classMap = new HashMap<>();
    public static void putContext(String key,JavaClass javaClass){
        classMap.put(key, javaClass);
    }
    public static JavaClass getClass(String className){
        return classMap.get(className);
    }
}
