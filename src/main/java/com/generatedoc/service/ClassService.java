package com.generatedoc.service;

import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.ParameterDesc;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaParameter;

import java.util.List;

public interface ClassService {


    List<ClassFieldDesc> getJavaClassDesc(JavaParameter parameter);
}
