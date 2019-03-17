package com.generatedoc.service;

import com.generatedoc.model.ClassInfo;
import com.generatedoc.model.HeadParameterInfo;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

import java.util.List;

public interface ParameterService {


    List<HeadParameterInfo> headerParameterToDoc(JavaParameter parameter, JavaMethod javaMethod);

    ClassInfo bodyParameterToDoc(JavaParameter parameter, JavaMethod javaMethod);

    boolean isBodyParameter(JavaParameter parameter);
}
