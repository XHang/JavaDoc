package com.generatedoc.util.com.generatedoc.main;

import com.generatedoc.util.FileUtil;
import com.thoughtworks.qdox.model.JavaClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApplicationTest {

    //@Test
    public void getJavaFilesByPathTest(){
        //TODO 测试代码完蛋了
        String path = "D:\\gddxit-project\\新的的\\wwis-meterreading-service\\wwis-install\\src\\main\\java\\com\\gddxit\\wwis\\installreport\\control";
        List<JavaClass>javaClasses =new ArrayList<>();
        List<File> javaFiles = null;//.getJavaFilesByPath(path);
        javaFiles.forEach((file)->{
            System.out.println("取出来的java文件名是"+file.getName());
            if ("StepDecisionController.java".equals(file.getName())){
                System.out.println("catch it");
            }
            if ("BudgetControl.java".equals(file.getName())){
                System.out.println("catch it");
            }
            FileUtil.getControlClass(file,javaClasses);
            return ;
        });
        javaClasses.forEach((javaClass -> {
            System.out.println("取出来的控制器类名是" + javaClass.getName());
            System.out.println("类的注释是:"+javaClass.getComment());
            return;
        }
        ));
    }
}
