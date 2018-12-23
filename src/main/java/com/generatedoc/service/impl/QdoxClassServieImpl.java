package com.generatedoc.service.impl;

import com.generatedoc.constant.ValidConstant;
import com.generatedoc.emnu.DataType;
import com.generatedoc.emnu.RuleType;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.FieldRule;
import com.generatedoc.service.ClassService;
import com.generatedoc.util.AnnotationUtil;
import com.thoughtworks.qdox.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class QdoxClassServieImpl implements ClassService {
     public static final Logger log = LoggerFactory.getLogger(QdoxClassServieImpl.class);
    @Override
    public List<ClassFieldDesc> getJavaClassDesc( JavaParameter parameter) {
        JavaClass clazz = parameter.getJavaClass();
        //TODO getField和getBeanProperties有何不同呢？
        List<JavaField> fields =  clazz.getFields();
        List<ClassFieldDesc> result = new ArrayList<>();
        for (JavaField field : fields) {
            ClassFieldDesc desc = new ClassFieldDesc();
            desc.setDataType(getDataTypeByJavaType(field));
            desc.setParameterDesc(getParameterDesc(field,clazz));
            desc.setParameterName(field.getName());
            if (isLimitParameter(parameter)){
                List<FieldRule> rule = getRule(field);
                desc.setFieldRule(getRule(field));
            }
        }
        return null;
    }
    private String getLimitGroup(JavaParameter parameter){
       List<JavaAnnotation> annotations = parameter.getAnnotations();
        return null;
    }

    private List<FieldRule> getRule(JavaField field) {
        List<FieldRule> result = new ArrayList<>();
        List<JavaAnnotation> annotations = field.getAnnotations();
        //需要考虑group的可能性
        FieldRule fieldRule = buildRequired(annotations);
        if (fieldRule != null) {
            result.add(fieldRule);
        }

         fieldRule = buildMaxLimit(annotations);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        fieldRule = buildMinLimit(annotations);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        fieldRule = buildReg(annotations);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        return result;
    }

    private FieldRule buildRequired(List<JavaAnnotation> annotations) {
        if (isRequired(annotations)){
            FieldRule rule = new FieldRule();
            rule.setRuleType(RuleType.NOT_NULL);
            return rule;
        }else{
            return null;
        }
    }

    private FieldRule buildReg(List<JavaAnnotation> annotations) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.REG,annotations);
        if (annotation == null){
            return null;
        }
        FieldRule fieldRule = new FieldRule();
        fieldRule.setRuleType(RuleType.REG);
        fieldRule.setRuleValue(annotation.getNamedParameter(ValidConstant.REG_VALUE)+"");
        return fieldRule;
    }

    private FieldRule buildMaxLimit(List<JavaAnnotation> annotations) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.MAX_LIMIT,annotations);
        if (annotation == null){
            return null;
        }
        FieldRule rule = new FieldRule();
        Long value  = Long.valueOf(annotation.getNamedParameter(ValidConstant.LENGTH_VALUE)+"");
        rule.setMaxLength(value);
        rule.setRuleType(RuleType.MAX_LENGTH_LIMIT);
        return rule;
    }

    private FieldRule buildMinLimit(List<JavaAnnotation> annotations) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.MIN_LIMIT,annotations);
        if (annotation == null){
            return null;
        }
        FieldRule rule = new FieldRule();
        Long value  = Long.valueOf(annotation.getNamedParameter(ValidConstant.LENGTH_VALUE)+"");
        rule.setMinLength(value);
        rule.setRuleType(RuleType.MIN_LENGTH_LIMIT);
        return rule;
    }

    private boolean isLengthLimit(List<JavaAnnotation> annotations) {
        return AnnotationUtil.isExistAnnotation(ValidConstant.MAX_LIMIT,annotations) ||
                AnnotationUtil.isExistAnnotation(ValidConstant.MIN_LIMIT,annotations);
    }

    private boolean isRequired(List<JavaAnnotation> annotations) {
        return AnnotationUtil.isExistAnnotation("NotNull",annotations)
                || AnnotationUtil.isExistAnnotation("NotBlank",annotations)
                ||  AnnotationUtil.isExistAnnotation("NotEmpty",annotations);
    }

    private String getParameterDesc(JavaField field, JavaClass clazz) {
       String fieldDesc =  field.getComment();
       log.debug("字段名{}的描述是{}",field.getName(),fieldDesc);
      return fieldDesc;
    }

    private DataType getDataTypeByJavaType(JavaField field){
        //TODO 预期String,boolean,实际未知
       String typeName = field.getType().getName();
       log.debug("属性{}的数据类型是{}",field.getName(),typeName);
      return  DataType.valueOf(typeName);
    }

    private boolean isLimitParameter(JavaParameter parameter) {
        List<JavaAnnotation> annotations = parameter.getAnnotations();
        return AnnotationUtil.isExistAnnotation("@Validated",annotations);
    }
}
