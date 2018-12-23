package com.generatedoc.emnu;

public enum  YesOrNo {
    YES("是"),NO("否");

    private String name;


    YesOrNo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
