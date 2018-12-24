package com.generatedoc.filter;

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
        logger.debug("已扫描文件或文件夹："+filName);
        return FileUtil.isJavaFile(file);
    }

    /**
     * 暂时不知道此方法的作用,但是也没调用过
     * @param dir
     * @param name
     * @return
     */
    @Override
    public boolean accept(File dir, String name) {
        logger.info("当前扫描的文件夹名称为："+dir.getName());
        logger.info("name");
        throw new RuntimeException("出现该BUG请及时通知我");
    }
}
