package com.generatedoc.main;

import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.entity.APIDocument;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.filter.JavaFileFilter;
import com.generatedoc.service.*;
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

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private ContextService contextService;
    @Autowired
    private ClassService classService;

    public  void run (){
    /*    System.out.println("请输入文件夹路径");
        String path = IOUtil.getInput();
        System.out.println("请输入生成接口文件的路径");
        String targetpath = IOUtil.getInput();*/
        String path = "D:\\gddxit-project\\kunming-marketing-service";
        List<File> files = getJavaFilesByPath(path);
        contextService.setContext(files);
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
      files.forEach(file -> classService.getControlClass(file,javaClasses));
      List<APIDocument> apiDocuments = new ArrayList<>();
      //将对外接口类转成接口文档对象
       javaClasses.forEach((javaClass -> apiDocuments.add(controllerService.generationApiDocment(javaClass))));
       return apiDocuments;
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
