package com.generatedoc.entity;

import com.generatedoc.emnu.RuleType;

/**
 * bean类的字段规则
 */
public class FieldRule {

    private RuleType ruleType;
    private String ruleValue;
    private Long maxLength;
    private Long minLength;

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public Long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    public Long getMinLength() {
        return minLength;
    }

    public void setMinLength(Long minLength) {
        this.minLength = minLength;
    }
}
