package com.generatedoc.template;

import com.generatedoc.main.Application;
import com.generatedoc.util.FileUtil;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class JavaFileFilter implements IOFileFilter {

    private static Logger logger = LogManager.getLogger(JavaFileFilter.class);
    @Override
    public boolean accept(File file) {
        String filName = file.getName();
        logger.info("已扫描文件："+filName);
        return true;
    }

    @Override
    public boolean accept(File dir, String name) {
        logger.info("当前扫描的文件夹名称为："+dir.getName());
        logger.info("name");
        return true;
    }
}
