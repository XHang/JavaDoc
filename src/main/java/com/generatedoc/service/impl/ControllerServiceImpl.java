package com.generatedoc.service.impl;

import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.service.ControllerService;
import com.generatedoc.service.MethodService;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ControllerServiceImpl implements ControllerService {

    @Autowired
    private MethodService methodService;

    @Override
    public boolean isControlAnnotation(String annotatianName) {
        return SpringMVCConstant.CONTROLLER_NAMES.contains(annotatianName);
    }

    @Override
    public List<ApiInterface> buildMethodsDoc(List<JavaMethod> interfaceMethod) {
        List<ApiInterface> interfaces  = methodService.methodsToDoc(interfaceMethod);
        return interfaces;
    }

    @Override
    public List<JavaMethod> getInterfaceMethod(JavaClass javaClass) {
        List<JavaMethod> interfaceMethods = new ArrayList<>();
        List<JavaMethod> methods = javaClass.getMethods();
        if (CollectionUtils.isEmpty(methods)){
            log.warn("该接口类{}无方法可用",javaClass.getName());
            return new ArrayList<>();
        }
        methods.forEach(method -> {
            if (methodService.isInterfactMethod(method)){
                interfaceMethods.add(method);
            }
        });
        return interfaceMethods;
    }

    @Override
    public String buildTitle(JavaClass javaClass) {
        DocletTag docletTag  = javaClass.getTagByName("Title");
        String title = "";
        if (docletTag == null){
            log.info("找不到该类{}的Title注释信息",javaClass.getName());
            title =  javaClass.getComment();
        }else{
            title =  docletTag.getValue();
        }
        if (StringUtil.isEmpty(title)){
            log.warn("类名{}找不到有效的接口文档描述，取类名为接口文档名");
            title = javaClass.getName();
        }
        return title;
    }

    @Override
    public String buildAuthor(JavaClass javaClass) {
        DocletTag docletTag = javaClass.getTagByName("author");
        if (docletTag == null){
            log.info("找不到该类{}的作者注释信息",javaClass.getName());
            return "";
        }
        String author = docletTag.getValue();
        log.info("该类{}的author注释是{}",javaClass.getName(),author);
        return author;
    }
}
