package com.generatedoc.main;

import com.generatedoc.entity.APIDocument;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.exception.DOCError;
import com.generatedoc.template.JavaFileFilter;
import com.generatedoc.util.FileUtil;
import com.generatedoc.util.IOUtil;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author
 */
public class Application {

    private static Logger logger = LogManager.getLogger(Application.class);

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
      files.forEach(file -> FileUtil.getControlClass(file,javaClasses));
      List<APIDocument> apiDocuments = new ArrayList<>();
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
       document.setDocumentName(Optional.ofNullable(javaClass.getTagByName("Title").getValue()).orElse(javaClass.getComment()));
       document.setInterfaceDesc(Optional.ofNullable(javaClass.getComment()).orElse("无描述，请充分发挥你的想象力"));
       return document;
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

   }
    public static void saveDoc(String descPath,APIDocument apiDocument){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(descPath));

        } catch (Exception e) {
           throw new DOCError("保存接口文档时发生异常",e);

        }finally {

        }
    }
}
