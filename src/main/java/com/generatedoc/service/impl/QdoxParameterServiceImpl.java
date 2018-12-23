package com.generatedoc.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.ParameterDesc;
import com.generatedoc.exception.DOCError;
import com.generatedoc.service.ClassService;
import com.generatedoc.service.ParameterService;
import com.generatedoc.util.AnnotationUtil;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class QdoxParameterServiceImpl implements ParameterService {

     public static final Logger log = LoggerFactory.getLogger(QdoxParameterServiceImpl.class);
    private ClassService classService;

    @Override
    public List<ParameterDesc> parameterToDoc(JavaParameter parameter, JavaMethod javaMethod) {
        if(isBodyParameter(parameter)){
            return bodyParameterToDoc(parameter);
        }
        return headerParameterToDoc(parameter,javaMethod);
    }

    private List<ParameterDesc> headerParameterToDoc(JavaParameter parameter, JavaMethod javaMethod) {
        //TODO 未完成
        return null;
    }

    private List<ParameterDesc> bodyParameterToDoc(JavaParameter parameter) {
        try {
            List<ClassFieldDesc> parameterDescs = classService.getJavaClassDesc(parameter);
            List<ParameterDesc> result = new ArrayList<>();
            if (CollectionUtils.isEmpty(parameterDescs)){
                return result;
            }
            for (ClassFieldDesc parameterDesc : parameterDescs) {
                ParameterDesc desc = new ParameterDesc();
                BeanUtils.copyProperties(desc,parameterDesc);
                result.add(desc);
            }
            return result;
        } catch (Exception e) {
            throw new DOCError("抽取请求体参数信息失败",e);
        }
    }



    private boolean isBodyParameter(JavaParameter parameter) {
        log.debug("开始判断参数{}是否是请求体参数",parameter.getName());
        List<JavaAnnotation> annotations = parameter.getAnnotations();
        if (CollectionUtils.isEmpty(annotations)){
            return false;
        }
        for (JavaAnnotation annotation : annotations) {
           JavaClass javaClass =  annotation.getType();
            log.debug("遍历参数{}的注解，注解类名为{}",parameter.getName(),javaClass.getName());
           //TODO 有可能返回包名，所以要小心点
           if ("RequestBody".equals(javaClass.getName())){
               log.debug("参数名{}有RequestBody注解，因此是请求体参数",parameter.getName());
               return true;
           }
        }
        log.debug("参数名{}没有RequestBody注解，因此不是请求体参数",parameter.getName());
        return false;
    }
}
