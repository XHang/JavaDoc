package com.generatedoc.service;

import com.generatedoc.entity.ParameterDesc;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

import java.util.List;

public interface ParameterService {
    /**
     * 将单个参数转成doc格式
     * @param parameter
     * @param javaMethod
     * @return
     * 首先判断请求参数是否是请求体。如果是，拿到里面的类型，然后遍历字段填充
     * 如果不是，那么就是放在URL的参数。
     */
    List<ParameterDesc> parameterToDoc(JavaParameter parameter, JavaMethod javaMethod);
}
