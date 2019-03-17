package com.generatedoc.emnu;

public enum RuleType {
    /**
     * 正则限制
     */
    REG("正则限制"),
    /**
     * 非空限制
     */
    NOT_NULL("非空限制"),
    /**
     * 最大长度限制
     */
    MAX_LENGTH_LIMIT("最大长度限制"),
    /**
     * 最小长度限制
     */
    MIN_LENGTH_LIMIT("最小长度限制"),

    SIZE_LENGTH_LIMIT("长度限制");

    private String name;

    RuleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
