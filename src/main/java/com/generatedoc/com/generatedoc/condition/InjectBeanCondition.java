package com.generatedoc.com.generatedoc.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * TODO Bean是否注入判断器
 */

public class InjectBeanCondition  {
    /**
     * 判断所给的类，在扩展包名内，是否已有同个父类的子类
     * @param interfactName
     * @return
     */
    public static boolean isExistSimilarClass(String interfactName){
        String packageName = getExtendPackageName();
        Package extPackage = Package.getPackage(packageName);
        return false;
    }

    /**
     * 得到扩展类存放的包名
     * @return
     */
    public static String  getExtendPackageName(){

        return null;
    }
}
