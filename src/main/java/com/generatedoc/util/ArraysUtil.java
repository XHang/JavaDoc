package com.generatedoc.util;

import java.util.Arrays;

public class ArraysUtil {

    public static  boolean isEmpty(Object[] objects){
        return objects == null || objects.length == 0;
    }

    public static boolean contains(Object[] objects, Object key){
       return  Arrays.asList(objects).contains(key);
    }
}
