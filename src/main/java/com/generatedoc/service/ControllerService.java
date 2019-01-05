package com.generatedoc.service;

import com.generatedoc.model.APIDocument;
import com.generatedoc.model.ApiInterface;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ControllerService {

     public static final Logger log = LoggerFactory.getLogger(ControllerService.class);

    boolean isControlAnnotation(String annotatianName);



    /**
     * 根据JavaClass，生成api文档
     * @param javaClass
     * @return APIDocument 接口文档DTO类
     */
    default APIDocument generationApiDocment(JavaClass javaClass){
        log.debug("开始抽取类{}的数据作为接口文档数据",javaClass.getName());
        APIDocument document = new APIDocument();
        document.setAuthor(buildAuthor(javaClass));
        document.setDate(LocalDateTime.now());
        document.setDocumentName(buildTitle(javaClass));
        document.setInterfaceDesc(Optional.ofNullable(javaClass.getComment()).orElse("无描述，请充分发挥你的想象力"));
        List<JavaMethod> interfaceMethod = getInterfaceMethod(javaClass);
        List<ApiInterface> interfaces  = buildMethodsDoc(interfaceMethod);
        document.setApiInterface(interfaces);
        return document;
    }

    /** 将对外暴露的接口方法转成文档数据
     * @param interfaceMethod
     * @return
     */
    List<ApiInterface> buildMethodsDoc(List<JavaMethod> interfaceMethod);

    /**
     * 抽取接口类里面对外的接口方法
     * @param javaClass
     * @return
     */
    List<JavaMethod> getInterfaceMethod(JavaClass javaClass);

    /**
     * 构建接口类标题信息
     * @param javaClass
     * @return
     */
    String buildTitle(JavaClass javaClass);

    /**
     * 构建接口类作者信息
     * @param javaClass
     * @return
     */
    String buildAuthor(JavaClass javaClass);
}
