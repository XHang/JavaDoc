package com.generatedoc.main;

import com.generatedoc.entity.APIDocument;

import java.io.File;
import java.util.List;

public class Application {
    public static void main (String[] args){

    }

    /**
     * 根据指定路径，取出对应的java文件
     * @param path 路径
     * @return java文件列表
     */
   public List<File> getJavaFilesByPath(String path){
        return null;
   }

    /**
     * 根据Java文件生成接口文档实体类
     * @param files java文件列表
     * @return 接口文档的实体类
     */
   public List<APIDocument> generateDoc(List<File> files){
       return null;
   }

    /**
     * 生成接口文档
     * @param descPath 目的文件夹
     * @param list 接口文档实体类列表
     */
   public void saveDoc(String descPath,List<APIDocument> list){

   }
}
