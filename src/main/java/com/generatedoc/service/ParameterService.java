package com.generatedoc.service;

import com.generatedoc.model.ClassDesc;
import com.generatedoc.model.HeadParameterDesc;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

import java.util.List;

public interface ParameterService {


    List<HeadParameterDesc> headerParameterToDoc(JavaParameter parameter, JavaMethod javaMethod);

    ClassDesc bodyParameterToDoc(JavaParameter parameter, JavaMethod javaMethod);

    boolean isBodyParameter(JavaParameter parameter);
}
