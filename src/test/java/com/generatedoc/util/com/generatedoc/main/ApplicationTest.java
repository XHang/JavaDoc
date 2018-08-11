package com.generatedoc.util.com.generatedoc.main;

import com.generatedoc.main.Application;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ApplicationTest {

    @Test
    public void getJavaFilesByPathTest(){
        String path = "D:\\gddxit-project\\Dayawan";
        List<File> javaFiles = Application.getJavaFilesByPath(path);
        javaFiles.forEach((file)->System.out.println("取出来的java文件名是"+file.getName()));

    }
}
