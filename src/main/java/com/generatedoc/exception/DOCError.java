package com.generatedoc.exception;

import com.generatedoc.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DOCError extends RuntimeException {
    private static Logger logger = LogManager.getLogger(FileUtil.class);

    public DOCError(String errorMsg){
        super(errorMsg);
        logger.error(errorMsg);
    }

}
