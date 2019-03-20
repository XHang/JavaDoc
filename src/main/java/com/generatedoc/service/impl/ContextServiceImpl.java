package com.generatedoc.service.impl;

import com.generatedoc.context.ClassContext;
import com.generatedoc.service.ContextService;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class ContextServiceImpl implements ContextService {
     public static final Logger log = LoggerFactory.getLogger(ContextServiceImpl.class);


    /**
     * 设置工作的上下文
     * @param files
     */
    @Override
    public void setContext(List<File> files) {
        List<JavaClass> classes = new ArrayList<>();
        for (File file : files) {
            List<JavaClass> clazzs = resolveJavaFile(file);
            if (CollectionUtils.isEmpty(clazzs)) {
                continue;
            }
            classes.addAll(clazzs);
        }
        setClassContext(classes);
    }

    private void setClassContext(List<JavaClass> classes) {
        if(CollectionUtils.isEmpty(classes)){
            log.warn("解析不到任何的java类存储进上下文");
            return;
        }
        for (JavaClass javaClass : classes) {
            String key = javaClass.getFullyQualifiedName();
            ClassContext.putContext(key,javaClass);
        }
    }

    private List<JavaClass> resolveJavaFile(File file) {
        try {
            JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
            JavaSource source = javaProjectBuilder.addSource(file);
            if (source == null){
                return null;
            }
            List<JavaClass> clazzs =  source.getClasses();
            return clazzs;
        } catch (IOException e) {
            log.error("解析文件到java类异常",e);
            return null;
        }
    }



}
