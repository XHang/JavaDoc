package com.generatedoc.condition;

import com.generatedoc.constant.SpringConstant;
import com.generatedoc.util.AnnotationUtil;
import com.generatedoc.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * TODO Bean是否注入判断器
 */

public class InjectBeanCondition  implements Condition{

     private List<Class> allClassOnPackage = new ArrayList<>();
     private final String packageName = "com.generatedoc.service.extimpl";
     public static final Logger log = LoggerFactory.getLogger(InjectBeanCondition.class);

     public InjectBeanCondition() throws Exception{
         List<File> files = getAllFileByPackageName(packageName);
         for (File file : files) {
             allClassOnPackage.addAll(getClassByFile(file,packageName));
         }
     }
     private static List<File> getAllFileByPackageName(String packageName)throws Exception{
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         Enumeration<URL> enumeration = classLoader.getResources(StringUtil.packageNameToDirName(packageName));
         List<File> files = new ArrayList<>();
         while(enumeration.hasMoreElements()){
             URL url = enumeration.nextElement();
             File file = new File(url.getFile());
             files.add(file);
         }
         return files;
     }
     private static List<Class> getClassByFile(File file,String packageName) throws ClassNotFoundException {
         List<Class> result = new ArrayList<>();
             if (file.isDirectory()){
                 for (File childFile : file.listFiles()) {
                     result.addAll(getClassByFile(childFile,packageName));
                 }

             }else{
                 String fullClassName = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                 result.add(Class.forName(fullClassName));
             }
         return result;
     }

    /**
     * 判断所给的类，在扩展包名内，是否已有同类
     * @param interfactName
     * @return
     */
    public  boolean isExistSimilarBean(Class clazz){
        for (Class aClass : allClassOnPackage) {
            //必须是加了clazz的本类或子类，以及是Bean
            if (clazz.isAssignableFrom(aClass) && isBean(aClass)){
                return true;
            }
        }
        return false;
    }

    private boolean isBean(Class aClass) {
        for (Annotation annotation : aClass.getAnnotations()) {
            String annouationName = AnnotationUtil.getSimpleClassName(annotation.getClass().getName());
            if (SpringConstant.BEAN_ANNOUATION.contains(annouationName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 得到扩展类存放的包名
     * @return
     */
    public static String  getExtendPackageName(){

        return null;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(SameClassCondition.class.getName(), false);
        if (attributes == null) {
            log.error("处理bean条件注入失败，使用默认bean注入");
            return false;
        }
        List<String> candidates = new ArrayList<>();
        List<Object> classes =attributes.get("value");
        Class clazz = (Class) classes.get(0);
        //扩展包含有和默认实现相同的类，则禁用默认实现
        return !isExistSimilarBean(clazz);
    }

}
