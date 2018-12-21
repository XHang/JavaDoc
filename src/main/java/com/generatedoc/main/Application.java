package com.generatedoc.main;

import com.generatedoc.entity.APIDocument;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.exception.DOCError;
import com.generatedoc.service.DocmentsService;
import com.generatedoc.template.JavaFileFilter;
import com.generatedoc.util.FileUtil;
import com.generatedoc.util.IOUtil;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author
 */
public class Application {

 public static final Logger log = LoggerFactory.getLogger(Application.class);
    private static DocmentsService docmentsService;

    public static void main (String[] args){
        System.out.println("请输入文件夹路径");
        String path = IOUtil.getInput();
        System.out.println("请输入生成接口文件的路径");
        String targetpath = IOUtil.getInput();
        List<File> files = getJavaFilesByPath(path);
        List<APIDocument> documents = generateDoc(files);
        saveDoc(targetpath,documents);

    }



    /**
     * 根据指定路径，取出对应的java文件
     * @param path 路径
     * @return java文件列表
     */
   public  static List<File> getJavaFilesByPath(String path){

        Collection<File> files = FileUtils.listFiles(new File(path),new JavaFileFilter(),TrueFileFilter.TRUE);
        return new ArrayList<>(files);
   }



    /**
     * 根据Java文件生成接口文档实体类
     * @param files java文件列表
     * @return 接口文档的实体类
     */
   public static  List<APIDocument> generateDoc(List<File> files){
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
   private static  APIDocument generationApiDocment(JavaClass javaClass){
       APIDocument document = new APIDocument();
       document.setAuthor(Optional.ofNullable(javaClass.getTagByName("author").getValue()).orElse("管理员"));
       document.setDate(LocalDateTime.now());
       //取类的注解@Title作为接口文档标题，如有没有的话，取类的注解作为标题
       document.setDocumentName(Optional.ofNullable(javaClass.getTagByName("Title").getValue()).orElse(javaClass.getComment()));
       //取类的注解作为接口文档描述
       document.setInterfaceDesc(Optional.ofNullable(javaClass.getComment()).orElse("无描述，请充分发挥你的想象力"));
       List<JavaMethod> interfaceMethod = getInterfaceMethod(javaClass);
       List<ApiInterface> interfaces = interfaceMethod2Doc(interfaceMethod);
       document.setApiInterface(interfaces);
       return document;
   }

   //将接口方法转成接口文档数据
    private static List<ApiInterface> interfaceMethod2Doc(List<JavaMethod> interfaceMethod) {
       //TODO 未完成
        return null;
    }

    private static List<JavaMethod> getInterfaceMethod(JavaClass javaClass) {
        List<JavaMethod> interfaceMethods = new ArrayList<>();
       List<JavaMethod> methods = javaClass.getMethods();
       if (CollectionUtils.isEmpty(methods)){
           log.warn("该接口类{}无方法可用",javaClass.getName());
           return new ArrayList<>();
       }
       methods.forEach(method -> {
           if (isInterfactMethod(method)){
               interfaceMethods.add(method);
           }
       });


        return interfaceMethods;
    }

    /**
     * 是否是对外接口的方法
     * @param method
     * @return
     */
    private static boolean isInterfactMethod(JavaMethod method) {
        //TODO
        return true;
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
   public static void saveDoc(String descPath,List<APIDocument> list){
        if (CollectionUtils.isEmpty(list)){
            log.warn("本次运行无生成任何接口文档");
            return;
        }
        list.forEach(apiDocument -> {
           docmentsService.saveDoc(apiDocument,descPath);
        });
   }
}
