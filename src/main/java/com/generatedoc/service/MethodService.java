package com.generatedoc.service;

import com.generatedoc.entity.ApiInterface;
import com.thoughtworks.qdox.model.JavaMethod;

import java.util.List;

public interface MethodService {
    public ApiInterface methodToDoc(JavaMethod javaMethod);

    List<ApiInterface> methodsToDoc(List<JavaMethod> interfaceMethod);

    boolean isInterfactMethod(JavaMethod method);
}
