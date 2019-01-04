package com.generatedoc.service.impl;

import com.generatedoc.emnu.DataType;
import com.generatedoc.entity.ClassDesc;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.HeadParameterDesc;
import com.generatedoc.exception.DOCError;
import com.generatedoc.service.ClassService;
import com.generatedoc.service.ParameterService;
import com.thoughtworks.qdox.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class QdoxParameterServiceImpl implements ParameterService {

     public static final Logger log = LoggerFactory.getLogger(QdoxParameterServiceImpl.class);
    @Autowired
     private ClassService classService;

    @Override
    public List<HeadParameterDesc> headerParameterToDoc(JavaParameter parameter, JavaMethod javaMethod) {
        //TODO 小心别名啦
        List<HeadParameterDesc> result = new ArrayList<>();

        if (isInnerParameter(parameter)){
            List<ClassFieldDesc> list = classService.getJavaClassDesc(parameter);
            for (ClassFieldDesc classFieldDesc : list) {
                HeadParameterDesc desc = converClassFieldDesc(classFieldDesc);
                result.add(desc);
            }
        }else{
            HeadParameterDesc desc = new HeadParameterDesc();
            desc.setDataType(classService.getDataType(parameter.getJavaClass()));
            desc.setParameterDesc(buildParameterDesc(parameter,javaMethod));
            desc.setParameterName(parameter.getName());
            //TODO  默认值，限制规则排计划
            result.add(desc);
        }
        return result;
    }

    private String buildParameterDesc(JavaParameter parameter, JavaMethod javaMethod) {
        DocletTag docletTag = javaMethod.getTagByName(parameter.getName());
        if (docletTag == null){
            log.warn("方法{}找不到参数{}的注释",javaMethod.getName(),parameter.getName());
            return "";
        }
        return docletTag.getValue();
    }

    /**
     * 判断是否是内部参数
     * 如果是内部请求参数，则要遍历字段名形成参数名
     * @return
     */
    private boolean isInnerParameter(JavaParameter parameter){
        JavaClass clazz = parameter.getJavaClass();
        DataType dataType =  classService.getDataType(clazz);
        return DataType.OBJECT.equals(dataType);
    }



    @Override
    public List<ClassDesc> bodyParameterToDoc(JavaParameter parameter, JavaMethod javaMethod) {
        try {
            List<ClassFieldDesc> parameterDescs = classService.getJavaClassDesc(parameter);
            List<HeadParameterDesc> result = new ArrayList<>();
            if (CollectionUtils.isEmpty(parameterDescs)){
                return result;
            }
            for (ClassFieldDesc parameterDesc : parameterDescs) {
                HeadParameterDesc desc = converClassFieldDesc(parameterDesc);
                result.add(desc);
            }
            return result;
        } catch (Exception e) {
            throw new DOCError("抽取请求体参数信息失败",e);
        }
    }
    public HeadParameterDesc converClassFieldDesc(ClassFieldDesc fieldDesc){
        try {
            HeadParameterDesc parameterDesc = new HeadParameterDesc();
            BeanUtils.copyProperties(parameterDesc,fieldDesc);
            return parameterDesc;
        } catch (Exception e) {
            log.error("转换fieldDesc到ParameterDesc失败",e);
            return null;
        }
    }


    /**
     * @param parameter
     * @return
     */
    @Override
    public boolean isBodyParameter(JavaParameter parameter) {
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
