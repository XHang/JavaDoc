package com.generatedoc.service.impl;

import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.emnu.RequestType;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.ParameterDesc;
import com.generatedoc.entity.ReturnFieldDesc;
import com.generatedoc.service.ClassService;
import com.generatedoc.service.MethodService;
import com.generatedoc.service.ParameterService;
import com.generatedoc.util.AnnotationUtil;
import com.generatedoc.util.ArraysUtil;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.omg.Dynamic.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.generatedoc.util.StringUtil.removeBothSideChar;

@Service
public class MethodServiceImpl implements MethodService {

     public static final Logger log = LoggerFactory.getLogger(MethodServiceImpl.class);
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ClassService classService;

    @Override
    public ApiInterface methodToDoc(JavaMethod javaMethod) {
        ApiInterface apiInterface = new ApiInterface();
        buildAuthor(javaMethod,apiInterface);
        apiInterface.setDesc(getInterfaceDesc(javaMethod));
        apiInterface.setRequestType(getRequestType(javaMethod));
        apiInterface.setUrl(getUrl(javaMethod));
        buildRequestDesc(javaMethod,apiInterface);
        buildResponseDesc(javaMethod,apiInterface);
        return apiInterface;
    }

    private String getInterfaceDesc(JavaMethod javaMethod) {
        String desc = javaMethod.getComment();
        if (StringUtil.isEmpty(desc)){
            log.warn("方法{}找不到合适的注释，取方法名作为方法的注释");
            desc = javaMethod.getName();
        }
        return desc;
    }

    private void buildResponseDesc(JavaMethod javaMethod, ApiInterface apiInterface) {
        try {
            if (!isResponseBody(javaMethod)){
                //暂时只支持Json序列化
                return;
            }
            List<ClassFieldDesc> fieldDescs =classService.getJavaClassDesc(javaMethod.getReturns());
            if (CollectionUtils.isEmpty(fieldDescs)){
                return;
            }
            List<ReturnFieldDesc> returnFieldDescs = new ArrayList<>();
            for (ClassFieldDesc classFieldDesc : fieldDescs) {
                ReturnFieldDesc fieldDesc = new ReturnFieldDesc();
                BeanUtils.copyProperties(fieldDesc,classFieldDesc);
                returnFieldDescs.add(fieldDesc);
            }
            apiInterface.setReturnFieldDesc(returnFieldDescs);
        } catch (Exception e) {
            log.error("处理返回值描述失败",e);
            return;
        }

    }

    /**
     * 判断Response注解是否对该方法生效
     * @param javaMethod
     * @return
     */
    private boolean isResponseBody(JavaMethod javaMethod) {
        JavaClass javaClass = javaMethod.getDeclaringClass();
        List<JavaAnnotation> classAnnotations = javaClass.getAnnotations();
        if (AnnotationUtil.isExistAnnotation(SpringMVCConstant.RESPONSE_BODY,classAnnotations)
                || AnnotationUtil.isExistAnnotation(SpringMVCConstant.REST_CONTROLLER,classAnnotations)){
            return true;
        }
        List<JavaAnnotation> methodAnnotation = javaMethod.getAnnotations();
        if (AnnotationUtil.isExistAnnotation(SpringMVCConstant.RESPONSE_BODY,methodAnnotation)){
            return true;
        }
        return false;

    }

    private String getUrl(JavaMethod javaMethod) {
        JavaClass javaClass = javaMethod.getDeclaringClass();
        List<JavaAnnotation> annotations = javaClass.getAnnotations();
        JavaAnnotation classAnnotation = AnnotationUtil.getAnnotationByName(SpringMVCConstant.REQUEST_ANNOTATION,annotations);
        String prefix = "";
        if (classAnnotation  != null) {
            prefix =  getUrl(classAnnotation);
        }
        JavaAnnotation methodAnnotation = getInterAnnotation(javaMethod);
        if (methodAnnotation == null){
            log.warn("警告：方法{}没有任何标识为对接接口的注解",javaMethod.getName());
            return prefix;
        }
       String suffix =  getUrl(methodAnnotation);
        return removeBothSideChar(prefix+suffix);
    }
    private String getUrl(JavaAnnotation annotation){
        List<String> urls = new ArrayList<>();
        try {
            urls = (List<String>) annotation.getNamedParameter(SpringMVCConstant.REQUEST_URL_VALUE);
        } catch (ClassCastException e) {
            log.error("取得注解{}里面的路径(path  or value)失败了",annotation.getType().getName());
            return "";
        }
        if (CollectionUtils.isEmpty(urls)){
            return "";
        }
        //只拿一个URL就够了吧
        return urls.get(0);
    }

    /**
     * 获取该方法里面能证明是对外接口的注解，GetMapping注解之类的
     * @param javaMethod
     * @return 如果无，返回null
     */
    public JavaAnnotation getInterAnnotation(JavaMethod javaMethod){
        List<JavaAnnotation> annotations = javaMethod.getAnnotations();
        for (String annotationStr : SpringMVCConstant.CONTROLLER_METHOD) {
            JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(annotationStr,annotations);
            if (annotation !=null){
                return annotation;
            }
        }
        return null;
    }

    /**
     * 获取请求方式
     * @param javaMethod
     * @return
     */
    private RequestType getRequestType(JavaMethod javaMethod) {
        //TODO 暂不支持多请求啦
        log.debug("开始判断方法{}的请求类型",javaMethod.getName());
        List<JavaAnnotation> annotations = javaMethod.getAnnotations();
        List<String> interAnnotations = SpringMVCConstant.CONTROLLER_METHOD;
        RequestType requestType = null;
        for (String requestAnnotation : interAnnotations) {
            if (AnnotationUtil.isExistAnnotation(requestAnnotation,annotations)){
                 requestType = SpringMVCConstant.REQUEST_TYPE_MAP.get(requestAnnotation);
                if (requestType == null){
                    JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(SpringMVCConstant.REQUEST_ANNOTATION,annotations);
                    if (annotation == null){
                        log.warn("该方法{}找不到请求方式",javaMethod.getName());
                    }else{
                        String methodName = getMethod(annotation);
                        requestType = RequestType.valueOf(methodName);
                    }

                }
            }
        }
        return requestType;
    }

    private String getMethod(JavaAnnotation annotation) {
        try {
            List<String> methods = (List<String>) annotation .getNamedParameter(SpringMVCConstant.REQUEST_METHOD);
            if (CollectionUtils.isEmpty(methods)){
                log.error("获取注解{}的请求方式失败，{}属性没有值",annotation.getType().getName(),SpringMVCConstant.REQUEST_METHOD);
                return RequestType.UNKNOWN.name();
            }
            return AnnotationUtil.getLastStrWithoutDot(methods.get(0));
        } catch (ClassCastException e) {
            log.error("获取注解{}的请求方式失败，{}属性不是字符串集合",annotation.getType().getName(),SpringMVCConstant.REQUEST_METHOD);
            return RequestType.UNKNOWN.name();
        }
    }

    private void buildRequestDesc(JavaMethod javaMethod, ApiInterface apiInterface) {
        List<JavaParameter> parameters =  javaMethod.getParameters();
        if (CollectionUtils.isEmpty(parameters)){
            return;
        }
        List<ParameterDesc> parameterDescs = new ArrayList<>();
        for (JavaParameter parameter : parameters) {
            List<ParameterDesc> descs = parameterService.parameterToDoc(parameter,javaMethod);
            parameterDescs.addAll(descs);
        }
        apiInterface.setParameters(parameterDescs);
    }



    private void buildAuthor(JavaMethod javaMethod, ApiInterface apiInterface) {
        DocletTag author = javaMethod.getTagByName("authod");
        if (author == null){
            apiInterface.setAuthor("  ");
            return;
        }
         apiInterface.setAuthor(author.getName());
    }

    @Override
    public List<ApiInterface> methodsToDoc(List<JavaMethod> interfaceMethod) {
       if (CollectionUtils.isEmpty(interfaceMethod)){
           return new ArrayList<>();
       }
        List<ApiInterface> datas = new ArrayList<>();
        for (JavaMethod javaMethod : interfaceMethod) {
            ApiInterface apiInterface =  methodToDoc(javaMethod);
            datas.add(apiInterface);
        }
        return datas;
    }

    /**
     * 是否是对外接口的方法
     * @param method
     * @return
     */
    @Override
    public  boolean isInterfactMethod(JavaMethod method) {
        JavaAnnotation annotation = getInterAnnotation(method);
        return annotation !=null;
    }
}
