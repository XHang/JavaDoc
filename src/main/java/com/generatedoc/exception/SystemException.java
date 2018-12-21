package com.generatedoc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统级错误
 */
public class SystemException extends RuntimeException{

    private static Logger log = LoggerFactory.getLogger(SystemException.class);

    /**
     * 构建一个简单的异常类，自动记录异常
     * @param errorMsg
     */
    public SystemException(String format, Object... args){
        super(String.format(format,args));
        String  errorMsg = String.format(format,args);
        log.error(errorMsg);
    }

    /**
     * 构建一个简单的异常类，记录异常
     * @param errorMsg 异常信息
     * @param e 异常栈信息
     */
    public SystemException(String errorMsg, Exception e){
        super(errorMsg,e);
        log.error(errorMsg,e);
    }
    /**
     * 构建一个简单的异常类，记录异常
     * @param errorMsg 异常信息
     * @param e 异常栈信息
     */
    public SystemException(Exception e, String format, Object... args){
        super( String.format(format,args),e);
        String  errorMsg = String.format(format,args);
        log.error(errorMsg,e);
    }

    public SystemException(Exception e){
        super(e);
    }
}
