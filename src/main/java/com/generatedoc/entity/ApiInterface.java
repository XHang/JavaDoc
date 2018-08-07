package com.generatedoc.entity;

import com.generatedoc.emnu.RequestType;

import java.util.List;

public class ApiInterface {

    /**
     * 接口的请求类型
     */
    private RequestType requestType;
    /**
     * 接口的作者
     */
    private String author;
    /**
     * 访问接口的URL
     */
    private String url;
    /**
     * 接口参数列表
     */
    private List<ParameterDesc> parameters;
    /**
     * 请求参数示例
     */
    private String parameterExample;
    /**
     * 接口的返回值类型
     */
    private String returnType;
    /**
     * 接口返回值的解释
     */
    private List<ReturnFieldDesc> returnFieldDesc;

}
