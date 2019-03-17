package com.generatedoc.emnu;

import java.sql.Time;

public enum DataType {
    STRING("字符串"),
    NUMBER("数字"),
    BOOLEAN("布尔值"),
    OBJECT("对象"),
    ARRAY("数组"),
    ENUM("枚举");
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
