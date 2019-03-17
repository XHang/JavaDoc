package com.generatedoc.service;

import com.generatedoc.emnu.RequestType;
import com.generatedoc.model.ApiInterface;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public interface MethodService {
     public static final Logger log = LoggerFactory.getLogger(MethodService.class);

    default public ApiInterface methodToDoc(JavaMethod javaMethod){
        log.debug("开始抽取方法{}的信息",javaMethod.getName());
        ApiInterface apiInterface = new ApiInterface();
        buildAuthor(javaMethod,apiInterface);
        apiInterface.setDesc(getInterfaceDesc(javaMethod));
        apiInterface.setRequestType(getRequestType(javaMethod));
        apiInterface.setUrl(getUrl(javaMethod));
        buildRequestDesc(javaMethod,apiInterface);
        buildResponseDesc(javaMethod,apiInterface);
        return apiInterface;
    }

    /**
     * 抽取接口响应的字段解释
     * @param javaMethod
     * @param apiInterface
     * @return
     */
    void buildResponseDesc(JavaMethod javaMethod, ApiInterface apiInterface);

    /**
     * 抽取方法里面参数的解释
     * @param javaMethod
     * @param apiInterface
     * @return
     */
    void buildRequestDesc(JavaMethod javaMethod, ApiInterface apiInterface);

    /**
     * 构建接口请求的URL
     * @param javaMethod
     * @return
     */
    String getUrl(JavaMethod javaMethod);

    /**
     * 构建接口方法的描述
     * @param javaMethod
     * @return
     */
    String getInterfaceDesc(JavaMethod javaMethod);

    /**
     * 构建接口方法的请求类型
     * @param javaMethod
     * @return
     */
    RequestType getRequestType(JavaMethod javaMethod);

    default List<ApiInterface> methodsToDoc(List<JavaMethod> interfaceMethod){
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
     * 判断此方法是否是接口方法
     * @param method
     * @return
     */
    boolean isInterfactMethod(JavaMethod method);

    /**
     * 构建接口方法的作者信息
     * @param javaMethod
     * @param apiInterface
     */
    void buildAuthor(JavaMethod javaMethod, ApiInterface apiInterface);
}
