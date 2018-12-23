package com.generatedoc.emnu;

public enum DataType {
    STRING("字符串"),
    NUMBER("数字"),
    BOOLEAN("布尔值"),
    OBJECT("对象");
    private String name;

    DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
