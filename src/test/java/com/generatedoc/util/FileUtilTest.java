package com.generatedoc.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

public class FileUtilTest {

    //@Test
    public void isJavaFileTest(){
        String filepath = "D:\\gddxit-project\\Dayawan\\wwis-service\\wwis-core\\src\\main\\java\\com\\gddxit\\wwis\\core\\common\\beans\\CustomArrayEditor.java";
        System.out.println(FileUtil.isJavaFile(new File(filepath)));
    }
}
