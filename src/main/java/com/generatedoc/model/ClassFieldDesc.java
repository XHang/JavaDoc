package com.generatedoc.model;

import com.generatedoc.emnu.DataType;

import java.util.List;

public class ClassFieldDesc {
    /**
     * 参数名
     */
    private String parameterName;
    /**
     * 参数描述
     */
    private String parameterDesc;
    /**
     * 参数类型
     */
    private DataType dataType;

    /**
     * 规则
     * @return
     */
    private List<FieldRule> fieldRule;

    public List<FieldRule> getFieldRule() {
        return fieldRule;
    }

    public void setFieldRule(List<FieldRule> fieldRule) {
        this.fieldRule = fieldRule;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterDesc() {
        return parameterDesc;
    }

    public void setParameterDesc(String parameterDesc) {
        this.parameterDesc = parameterDesc;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    /**
     *嵌套实体类的字段描述
     */
    private ClassDesc nestingClssDesc;

    public ClassDesc getNestingClssDesc() {
        return nestingClssDesc;
    }

    public void setNestingClssDesc(ClassDesc nestingClssDesc) {
        this.nestingClssDesc = nestingClssDesc;
    }
}
