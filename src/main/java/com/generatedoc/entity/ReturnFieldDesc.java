package com.generatedoc.entity;

import com.generatedoc.emnu.DataType;

/**
 * 响应的字段解释实体类
 */
public class ReturnFieldDesc {
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段解释
     */
    private String fieldDesc;
    /**
     * 字段类型
     */
    private DataType fieldType;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public DataType getFieldType() {
        return fieldType;
    }

    public void setFieldType(DataType fieldType) {
        this.fieldType = fieldType;
    }
}
