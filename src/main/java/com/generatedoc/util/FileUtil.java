package com.generatedoc.util;

import com.generatedoc.exception.DOCError;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {

    private static Logger logger = LogManager.getLogger(FileUtil.class);
    private static  final String JAVA_FILE_TYPE = "java";
    private static final List<String> CONTROLLER_NAMES = new ArrayList<>();
    static {
        CONTROLLER_NAMES.add("Controller");
        CONTROLLER_NAMES.add("RestController");
    }

    public static  boolean isJavaFile(File file){
        String fileName = file.getName();
        String type  = FilenameUtils.getExtension(fileName);
        return JAVA_FILE_TYPE.equals(type);
    }

    /**
     * 得到控制器类文件
     * 如果java文件里面包含控制器类，则它会包含进classs里面。
     * 如果没有，则不包含
     * @param javaFile java为文件
     * @param classs 存放控制器类的集合
     */
    public static void  getControlClass(File javaFile,List<JavaClass> classs){
        try {
            JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
            JavaSource source = javaProjectBuilder.addSource(javaFile);
            if (source == null){
                logger.info("这个java文件【"+javaFile.getName()+"]】没有有效的类，跳过解析");
                return ;
            }
            List<JavaClass> clazzs =  source.getClasses();
           for (JavaClass javaClass:clazzs){
               List<JavaAnnotation> annotations = javaClass.getAnnotations();
               for (JavaAnnotation javaAnnotation:annotations){
                   String annotatianName  = javaAnnotation.getType().getCanonicalName();
                   annotatianName = StringUtil.getClassNameForFullName(annotatianName);
                   if (isControlAnnotation(annotatianName)){
                       classs.add(javaClass);
                       break;
                   }
               }
           }
        } catch (IOException e) {
           throw new DOCError("在判断java文件是否是control时出错了，文件名是：【"+javaFile.getName()+"]");
        }

    }

    /**
     * 判断是不是控制器的注解
     * @param annotationName
     * @return
     */
    private static boolean isControlAnnotation(String annotationName){
        return CONTROLLER_NAMES.contains(annotationName);
    }
}
