package com.generatedoc.constant;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public class SpringConstant {
    public static final List<String> BEAN_ANNOUATION = new ArrayList<>();
    static {
        BEAN_ANNOUATION.add("Service");
        BEAN_ANNOUATION.add("Component");
        BEAN_ANNOUATION.add("Repository");
        BEAN_ANNOUATION.add("Controller");
    }
}
