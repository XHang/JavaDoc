package com.generatedoc.main;

import com.generatedoc.config.ApplicationConfig;
import com.generatedoc.model.APIDocument;
import com.generatedoc.model.ApiInterface;
import com.generatedoc.filter.JavaFileFilter;
import com.generatedoc.service.*;
import com.generatedoc.util.IOUtil;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @author
 */
@Component
public class DocGenerateStarter {

 public  final Logger log = LoggerFactory.getLogger(DocGenerateStarter.class);
    @Autowired
    private  DocmentsService docmentsService;
    @Autowired
    private  MethodService methodService;

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private ContextService contextService;
    @Autowired
    private ClassService classService;

    @Autowired
    private ApplicationConfig config;

    public  void run (){
        setConfigIfNotExist(config);
        List<File> files = getJavaFilesByPath(config.getScanPath());
        contextService.setContext(files);
        List<APIDocument> documents = generateDoc(files);
        saveDoc(config.getSavePath(),documents);
    }

    private void setConfigIfNotExist(ApplicationConfig config) {
        if(StringUtil.isEmpty(config.getSavePath())){
            log.warn("application配置文件没有配置接口文档保存位置，需要手动设置");
            log.info("请输入接口文档保存位置");
            String savePath= IOUtil.getInput();
            config.setSavePath(savePath);
        }
        if (StringUtil.isEmpty(config.getScanPath())){
            log.warn("application配置文件没有配置接口文档扫描位置，需要手动设置");
            log.info("请输入接口文档扫描文件路径");
            String scanPath= IOUtil.getInput();
            config.setScanPath(scanPath);
        }
    }


    /**
     * 根据指定路径，取出对应的java文件
     * @param path 路径
     * @return java文件列表
     */
   public   List<File> getJavaFilesByPath(String path){

        Collection<File> files = FileUtils.listFiles(new File(path),new JavaFileFilter(),TrueFileFilter.TRUE);
        return new ArrayList<>(files);
   }



    /**
     * 根据Java文件生成接口文档实体类
     * @param files java文件列表
     * @return 接口文档的实体类
     */
   public   List<APIDocument> generateDoc(List<File> files){
       List<JavaClass> interfaceClasses  = getInterfaceClassByFiles(files);
      List<APIDocument> apiDocuments = getApiDocumentByClass(interfaceClasses);
       return apiDocuments;
   }

    /**
     *  将文件集合里面的对外接口类抽取出来，并返回
     * @param files
     * @return
     */
   public   List<JavaClass> getInterfaceClassByFiles(List<File> files){
       List<JavaClass> javaClasses = new ArrayList<>();
       for (File file : files) {
           javaClasses.addAll(classService.getControlClass(file));
       }
       return javaClasses;
   }

    /**
     * 将对外接口类转成接口文档对象
     * @param classes
     * @return
     */
   public List<APIDocument> getApiDocumentByClass(List<JavaClass> classes){
       List<APIDocument> result = new ArrayList<>();
       for (JavaClass clazz : classes) {
           result.add(controllerService.generationApiDocment(clazz));
       }
       return result;
   }









    /**
     * 根据方法信息，组装接口文档
     * @param method 方法内容
     * @return  ApiInterface 接口文档实体
     */
   private ApiInterface assembleInterfaceDoc(JavaMethod method){
       ApiInterface apiInterface = new ApiInterface();
       return apiInterface;

   }


    /**
     * 生成接口文档
     * @param descPath 目的文件夹
     * @param list 接口文档实体类列表
     */
   public  void saveDoc(String descPath,List<APIDocument> list){
        if (CollectionUtils.isEmpty(list)){
            log.warn("本次运行无生成任何接口文档");
            return;
        }
        list.forEach(apiDocument -> {
           docmentsService.saveDoc(apiDocument,descPath);
        });
   }
}
