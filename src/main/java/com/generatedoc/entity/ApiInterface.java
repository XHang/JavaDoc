package com.generatedoc.entity;

import com.generatedoc.emnu.RequestType;

import java.time.LocalDateTime;
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

    /**
     * 返回值示例。
     */
    private String returnExample;

    /**
     * 接口描述
     */
    private String desc;

    /**
     * 接口编写日期
     */
    private LocalDateTime time;


    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ParameterDesc> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDesc> parameters) {
        this.parameters = parameters;
    }

    public String getParameterExample() {
        return parameterExample;
    }

    public void setParameterExample(String parameterExample) {
        this.parameterExample = parameterExample;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<ReturnFieldDesc> getReturnFieldDesc() {
        return returnFieldDesc;
    }

    public void setReturnFieldDesc(List<ReturnFieldDesc> returnFieldDesc) {
        this.returnFieldDesc = returnFieldDesc;
    }

    public String getReturnExample() {
        return returnExample;
    }

    public void setReturnExample(String returnExample) {
        this.returnExample = returnExample;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
