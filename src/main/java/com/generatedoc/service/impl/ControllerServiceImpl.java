package com.generatedoc.service.impl;

import com.generatedoc.config.ApplicationConfig;
import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.model.ApiInterface;
import com.generatedoc.service.ClassService;
import com.generatedoc.service.ControllerService;
import com.generatedoc.service.MethodService;
import com.generatedoc.util.ArraysUtil;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//@Service
public class ControllerServiceImpl implements ControllerService {

    @Autowired
    private MethodService methodService;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private ClassService classService;



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
            if (methodService.isInterfactMethod(method) && isExistFilter(method)){
                interfaceMethods.add(method);
            }
        });
        return interfaceMethods;
    }

    private boolean isExistFilter(JavaMethod method) {
        log.debug("判断方法{}是否处于过滤集合内",method.getName());
       String fullName =  method.getDeclaringClass().getFullyQualifiedName();
       String[] filters = config.getFilters();
       if (ArraysUtil.isEmpty(filters)){
           log.debug("由于过滤集合为空，将生成方法的{}接口文档",method.getName());
           return true;
       }
       if (ArraysUtil.contains(filters,fullName)){
           log.debug("由于方法所属的类{}在过滤集合内，将生成方法的{}接口文档",fullName,method.getName());
           return true;
       }
       fullName = fullName+"."+method.getName();
       if (ArraysUtil.contains(filters,fullName)){
           log.debug("由于方法全称{}在过滤集合内，将生成方法的{}接口文档",fullName,method.getName());
           return true;
       }
        log.debug("由于方法{}不在过滤集合内，不生成接口文档",method.getName());
       return false;

    }

    @Override
    public String buildTitle(JavaClass javaClass) {
        return classService.buildTitle(javaClass);

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
