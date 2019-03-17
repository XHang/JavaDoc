package com.generatedoc.model;

import com.generatedoc.emnu.DataType;

import java.util.List;

/**
 * 一个参数的描述
 */
public class ClassInfo {

    private DataType dataType;
    /**
     * 类的描述
     */
    private String classDesc;
    /**
     * 类的字段解释
     */
    private List<ClassFieldInfo> classFieldInfos;

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public List<ClassFieldInfo> getClassFieldInfos() {
        return classFieldInfos;
    }

    public void setClassFieldInfos(List<ClassFieldInfo> classFieldInfos) {
        this.classFieldInfos = classFieldInfos;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
