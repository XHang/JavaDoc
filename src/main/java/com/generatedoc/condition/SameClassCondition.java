package com.generatedoc.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(InjectBeanCondition.class)
public @interface SameClassCondition {
    Class value() ;
}
