package com.generatedoc.entity;

/**
 * 接口参数
 */
public class ParameterDesc {

    /**
     * 参数名
     */
    private String parameterName;

    /**
     * 参数描述
     */
    private String parameterDesc;
    /**
     * 是否必须
     */
    private boolean required;
    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 参数类型
     */
    private String parameterType;

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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
