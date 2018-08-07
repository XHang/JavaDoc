package com.generatedoc.entity;

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

}
