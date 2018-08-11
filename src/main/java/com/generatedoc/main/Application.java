package com.generatedoc.main;

import com.generatedoc.entity.APIDocument;
import com.generatedoc.template.JavaFileFilter;
import com.generatedoc.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Application {

    private static Logger logger = LogManager.getLogger(Application.class);

    public static void main (String[] args){

    }

    /**
     * 根据指定路径，取出对应的java文件
     * @param path 路径
     * @return java文件列表
     */
   public  static List<File> getJavaFilesByPath(String path){
        Collection<File> files = FileUtils.listFiles(new File(path),new JavaFileFilter(),null);
        return new ArrayList<>(files);
   }



    /**
     * 根据Java文件生成接口文档实体类
     * @param files java文件列表
     * @return 接口文档的实体类
     */
   public static  List<APIDocument> generateDoc(List<File> files){
       return null;
   }

    /**
     * 生成接口文档
     * @param descPath 目的文件夹
     * @param list 接口文档实体类列表
     */
   public static void saveDoc(String descPath,List<APIDocument> list){

   }
}
