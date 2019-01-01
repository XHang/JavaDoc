package com.generatedoc.service;

import com.generatedoc.emnu.DataType;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.ParameterDesc;
import com.generatedoc.exception.DOCError;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.JavaSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ClassService {


    public static final Logger logger = LoggerFactory.getLogger(ClassService.class);

    List<ClassFieldDesc> getJavaClassDesc(JavaParameter parameter);


    List<ClassFieldDesc> getJavaClassDesc(JavaClass javaClass);

    /**
     * 根据解析器的数据类型，匹配文档的数据类型
     * @param clazz
     * @return
     */
    DataType getDataType(JavaClass clazz);

   default void getControlClass(File javaFile, List<JavaClass> classs) {
       try {
           logger.debug("判断Java文件【{}】里面的类是否是控制器类", javaFile.getName());
           JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
           //将该文件进行Java语法树分析，得到Java文件解析对象
           JavaSource source = javaProjectBuilder.addSource(javaFile);
           if (source == null) {
               logger.info("这个java文件【" + javaFile.getName() + "]】没有有效的类，跳过解析");
               return;
           }

           List<JavaClass> clazzs = source.getClasses();
           for (JavaClass javaClass : clazzs) {
               if (isInterfactClass(javaClass) && isNotExist(javaClass)) {
                   classs.add(javaClass);
               }
           }
       }catch(Exception e){

       }
   }

    default boolean isNotExist(JavaClass javaClass){

        return false;
    }

    public boolean isInterfactClass(JavaClass javaClass);
}
