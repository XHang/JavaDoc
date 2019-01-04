package com.generatedoc.entity;

import java.util.List;

/**
 * 一个实体类的描述
 */
public class ClassDesc {

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
}
