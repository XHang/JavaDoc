package com.generatedoc.service;

import com.generatedoc.entity.ClassDesc;
import com.generatedoc.entity.HeadParameterDesc;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

import java.util.List;

public interface ParameterService {


    List<HeadParameterDesc> headerParameterToDoc(JavaParameter parameter, JavaMethod javaMethod);

    List<ClassDesc> bodyParameterToDoc(JavaParameter parameter, JavaMethod javaMethod);

    boolean isBodyParameter(JavaParameter parameter);
}
