package com.generatedoc.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * web接口文档的实体类
 * 通过此类可以生成一份接口文档
 */
public class APIDocument {
    /**
     * 接口文档名
     * 通过类上的注释获取
     */
    private String documentName;

    /**
     * 接口文档描述
     * 通过类上的注释获取
     */
    private String interfaceDesc;

    /**
     * 接口文档编写者
     * 从类上的@Author注解抽取
     */
    private String author;

    /**
     * 文档生成日期
     */
    private LocalDateTime date;



    /**
     * api接口，可能有多个
     */
    private List<ApiInterface> apiInterface;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ApiInterface> getApiInterface() {
        return apiInterface;
    }

    public void setApiInterface(List<ApiInterface> apiInterface) {
        this.apiInterface = apiInterface;
    }
}
