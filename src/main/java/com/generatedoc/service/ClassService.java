package com.generatedoc.service;

import com.generatedoc.emnu.DataType;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.ParameterDesc;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaParameter;

import java.util.List;

public interface ClassService {


    List<ClassFieldDesc> getJavaClassDesc(JavaParameter parameter);


    List<ClassFieldDesc> getJavaClassDesc(JavaClass javaClass);

    /**
     * 根据解析器的数据类型，匹配文档的数据类型
     * @param javaClass
     * @return
     */
    DataType getDataTypeByJavaClass(JavaClass javaClass);
}
