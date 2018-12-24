package com.generatedoc.main;

import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.entity.APIDocument;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.filter.JavaFileFilter;
import com.generatedoc.service.DocmentsService;
import com.generatedoc.service.MethodService;
import com.generatedoc.util.AnnotationUtil;
import com.generatedoc.util.FileUtil;
import com.generatedoc.util.IOUtil;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaAnnotation;
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
import java.time.LocalDateTime;
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

    public  void run (){
    /*    System.out.println("请输入文件夹路径");
        String path = IOUtil.getInput();
        System.out.println("请输入生成接口文件的路径");
        String targetpath = IOUtil.getInput();*/
    String path = "D:\\gddxit-project\\kunming-marketing-service\\wwis-flowapplication";
    
        List<File> files = getJavaFilesByPath(path);
        List<APIDocument> documents = generateDoc(files);
        String targetpath = "E:\\工作目录\\昆明\\接口文档\\setting接口文档生成";
        saveDoc(targetpath,documents);

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
      List<JavaClass> javaClasses = new ArrayList<>();
      //将文件集合里面的对外接口类抽取出来，填充到javaClasses里面
      files.forEach(file -> FileUtil.getControlClass(file,javaClasses));
      List<APIDocument> apiDocuments = new ArrayList<>();
      //将对外接口类转成接口文档对象
       javaClasses.forEach((javaClass -> apiDocuments.add(generationApiDocment(javaClass))));
       return apiDocuments;
   }

    /**
     * 根据JavaClass，生成api文档
     * @param javaClass
     * @return
     */
   private   APIDocument generationApiDocment(JavaClass javaClass){
       log.debug("开始生成类{}的接口文档",javaClass.getName());
       APIDocument document = new APIDocument();
       document.setAuthor(buildAuthor(javaClass));
       document.setDate(LocalDateTime.now());
       //取类的注解@Title作为接口文档标题，如有没有的话，取类的注解作为标题
       document.setDocumentName(buildTitle(javaClass));
       //取类的注解作为接口文档描述
       document.setInterfaceDesc(Optional.ofNullable(javaClass.getComment()).orElse("无描述，请充分发挥你的想象力"));
       List<JavaMethod> interfaceMethod = getInterfaceMethod(javaClass);
       List<ApiInterface> interfaces  = methodService.methodsToDoc(interfaceMethod);
       document.setApiInterface(interfaces);
       return document;
   }

    private String buildTitle(JavaClass javaClass) {
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

    private String buildAuthor(JavaClass javaClass) {
        DocletTag docletTag = javaClass.getTagByName("author");
        if (docletTag == null){
            log.info("找不到该类{}的作者注释信息",javaClass.getName());
            return "";
        }
        String author = docletTag.getValue();
        log.info("该类{}的author注释是{}",javaClass.getName(),author);
        return author;
    }

    //将接口方法转成接口文档数据
    private  List<ApiInterface> interfaceMethod2Doc(List<JavaMethod> interfaceMethod) {

        return null;
    }

    private  List<JavaMethod> getInterfaceMethod(JavaClass javaClass) {
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
