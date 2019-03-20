package com.generatedoc.service.impl;

import com.generatedoc.constant.CommentConstant;
import com.generatedoc.constant.JavaConstant;
import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.constant.ValidConstant;
import com.generatedoc.emnu.DataType;
import com.generatedoc.model.ClassInfo;
import com.generatedoc.model.ClassFieldInfo;
import com.generatedoc.model.HeadParameterInfo;
import com.generatedoc.exception.DOCError;
import com.generatedoc.service.ClassService;
import com.generatedoc.service.ParameterService;
import com.generatedoc.util.AnnotationUtil;
import com.generatedoc.util.JSONUtil;
import com.thoughtworks.qdox.library.JavaClassContext;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QdoxParameterServiceImpl implements ParameterService {

     public static final Logger log = LoggerFactory.getLogger(QdoxParameterServiceImpl.class);
    @Autowired
     private ClassService classService;

    @Override
    public List<HeadParameterInfo> headerParameterToDoc(JavaParameter parameter, JavaMethod javaMethod) {
        //TODO 小心别名啦
        List<HeadParameterInfo> result = new ArrayList<>();

        if (isInnerParameter(parameter)){
            List<ClassFieldInfo> list = classService.getFieldDescForHeadParameter(parameter.getJavaClass());
            for (ClassFieldInfo classFieldInfo : list) {
                HeadParameterInfo desc = converClassFieldDesc(classFieldInfo);
                result.add(desc);
            }
        }else{
            HeadParameterInfo desc = new HeadParameterInfo();
            desc.setDataType(classService.getDataType(parameter.getJavaClass()));
            desc.setParameterDesc(buildParameterDesc(parameter,javaMethod));
            desc.setParameterName(parameter.getName());
            //TODO  默认值，限制规则排计划
            result.add(desc);
        }
        return result;
    }

    private String buildParameterDesc(JavaParameter parameter, JavaMethod javaMethod) {
        List<DocletTag> docletTags = javaMethod.getTagsByName(CommentConstant.PARAMETER);
        if (CollectionUtils.isEmpty(docletTags)){
            log.warn("方法{}找不到参数{}的注释",javaMethod.getName(),parameter.getName());
            return "";
        }
        for (DocletTag docletTag : docletTags) {
            String cmment = docletTag.getValue();
            if (cmment.contains(parameter.getName())){
                return cmment.replace(parameter.getName(),"").trim();
            }
        }
        log.warn("方法{}找不到参数{}的注释",javaMethod.getName(),parameter.getName());
        return "";
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
    public ClassInfo bodyParameterToDoc(JavaParameter parameter, JavaMethod javaMethod) {
        try {
            ClassInfo classInfo =  buildParameterInfo(parameter);
           List<ClassFieldInfo> fieldInfos  = classService.getJavaClassDescForBodyParameter(getRealJavaClass(parameter),isLimitParameter(parameter),getGroupName(parameter));
           classInfo.setClassFieldInfos(fieldInfos);
           return classInfo;
        } catch (Exception e) {
            throw new DOCError("抽取请求体参数信息失败",e);
        }

    }

    private JavaClass getRealJavaClass(JavaParameter parameter) {
        JavaClass target = null;
        if (DataType.ARRAY.equals(classService.getDataType(parameter.getJavaClass()))){
            if (parameter.getJavaClass().isArray()){
                return classService.getRealClass(parameter.getJavaClass());
            }
            DefaultJavaParameterizedType parameterizedType = (DefaultJavaParameterizedType) parameter.getType();
            List<JavaType> javaTypes = parameterizedType.getActualTypeArguments();
            if (CollectionUtils.isEmpty(javaTypes)){
               return null;
            }
            return (JavaClass) javaTypes.get(0);
        }
        return parameter.getJavaClass();
    }

    /**
     * 是否需要校验参数
     * @param parameter
     * @return
     */
    private boolean isLimitParameter(JavaParameter parameter) {
        List<JavaAnnotation> annotations = parameter.getAnnotations();
        String className =  AnnotationUtil.getSimpleClassName(parameter.getJavaClass().getName());
        //参数上面需要有@Validated 注解且类型不是集合类型
        return AnnotationUtil.isExistAnnotation(ValidConstant.VALID_ANNOUATION,annotations) && !JavaConstant.COLLECTION_TYPES.contains(className);
    }
    private  List<String> getGroupName(JavaParameter parameter){
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.VALID_ANNOUATION,parameter.getAnnotations());
        if (annotation == null) {
            return null;
        }
        List<String> groups = (List<String>) annotation.getNamedParameter("value");
        return groups;
    }

    private ClassInfo buildParameterInfo(JavaParameter parameter) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setDataType(classService.getDataType(parameter.getJavaClass()));
        classInfo.setClassDesc(Optional.ofNullable(parameter.getComment()).orElse(parameter.getName()));
        return classInfo;
    }

    public HeadParameterInfo converClassFieldDesc(ClassFieldInfo fieldDesc){
        try {
            HeadParameterInfo parameterDesc = new HeadParameterInfo();
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
           if (SpringMVCConstant.REQUEST_BODY.equals(AnnotationUtil.getSimpleClassName(javaClass.getName()))){
               log.debug("参数名{}有RequestBody注解，因此是请求体参数",parameter.getName());
               return true;
           }
        }
        log.debug("参数名{}没有RequestBody注解，因此不是请求体参数",parameter.getName());
        return false;
    }
}
