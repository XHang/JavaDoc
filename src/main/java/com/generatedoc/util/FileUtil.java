package com.generatedoc.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class FileUtil {

    private static  final String JAVA_FILE_TYPE = "java";

    public static  boolean isJavaFile(File file){
        String fileName = file.getName();
        String type  = FilenameUtils.getExtension(fileName);
        return JAVA_FILE_TYPE.equals(type);
    }
}
