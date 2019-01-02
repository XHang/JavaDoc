package com.generatedoc.util;

import com.generatedoc.exception.DOCError;
import com.generatedoc.filter.InterfactFilter;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {

    private static Logger logger = LogManager.getLogger(FileUtil.class);
    private static  final String JAVA_FILE_TYPE = "java";


    public static  boolean isJavaFile(File file){
        String fileName = file.getName();
        String type  = FilenameUtils.getExtension(fileName);
        return JAVA_FILE_TYPE.equals(type);
    }

}
