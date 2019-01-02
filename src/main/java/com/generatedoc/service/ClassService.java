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

    /**
     * 获取参数类型的类属性描述，
     * @param parameter
     * @return
     */
    List<ClassFieldDesc> getJavaClassDesc(JavaParameter parameter);

    /**
     * 获取类的类属性描述
     * @param javaClass
     * @return
     */
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
               if (isInterfactClass(javaClass) && isExist(javaClass)) {
                   classs.add(javaClass);
               }
           }
       }catch(Exception e){
           throw new DOCError("在判断java文件是否是control时出错了，文件名是：【"+javaFile.getName()+"]");
       }
   }

    /**
     * 判断类是否处于用户指定的过滤范围内
     * @param javaClass
     * @return
     */
     boolean isExist(JavaClass javaClass);

    /**
     * 判断类是否属于接口类，如Controller类
     * @param javaClass
     * @return
     */
     boolean isInterfactClass(JavaClass javaClass);
}
