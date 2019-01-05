package com.generatedoc.model;

import com.generatedoc.emnu.DataType;

import java.util.List;

/**
 * 一个参数类的描述
 */
public class ClassDesc {

    private DataType dataType;
    /**
     * 类的描述
     */
    private String classDesc;
    /**
     * 类的字段解释
     */
    private List<ClassFieldDesc> classFieldDescs;

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public List<ClassFieldDesc> getClassFieldDescs() {
        return classFieldDescs;
    }

    public void setClassFieldDescs(List<ClassFieldDesc> classFieldDescs) {
        this.classFieldDescs = classFieldDescs;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
