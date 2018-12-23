package com.generatedoc.service.impl;

import com.generatedoc.entity.ApiInterface;
import com.generatedoc.entity.ParameterDesc;
import com.generatedoc.service.MethodService;
import com.generatedoc.service.ParameterService;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import org.apache.commons.collections4.CollectionUtils;
import org.omg.Dynamic.Parameter;

import java.util.ArrayList;
import java.util.List;

public class MethodServiceImpl implements MethodService {
    private ParameterService parameterService;

    @Override
    public ApiInterface methodToDoc(JavaMethod javaMethod) {
        ApiInterface apiInterface = new ApiInterface();
        buildAuthor(javaMethod,apiInterface);
        apiInterface.setDesc(javaMethod.getComment());
        buildRequestDesc(javaMethod,apiInterface);
        return null;
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
        String author = javaMethod.getTagByName("authod").getName();
        if (StringUtil.isEmpty(author)){
            apiInterface.setAuthor("  ");
            return;
        }
        apiInterface.setAuthor(author);
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
}
