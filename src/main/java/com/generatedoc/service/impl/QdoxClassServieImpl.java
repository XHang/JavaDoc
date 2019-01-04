package com.generatedoc.service.impl;

import com.generatedoc.config.ApplicationConfig;
import com.generatedoc.constant.JavaConstant;
import com.generatedoc.constant.ValidConstant;
import com.generatedoc.context.ClassContext;
import com.generatedoc.emnu.DataType;
import com.generatedoc.emnu.RuleType;
import com.generatedoc.entity.ClassDesc;
import com.generatedoc.entity.ClassFieldDesc;
import com.generatedoc.entity.FieldRule;
import com.generatedoc.service.ClassService;
import com.generatedoc.service.ControllerService;
import com.generatedoc.util.AnnotationUtil;
import com.generatedoc.util.ArraysUtil;
import com.generatedoc.util.StringUtil;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QdoxClassServieImpl implements ClassService {
     public static final Logger log = LoggerFactory.getLogger(QdoxClassServieImpl.class);

     @Autowired
   private ControllerService controllerService;

     @Autowired
     ApplicationConfig config;

    @Override
    public List<ClassDesc> getJavaClassDescForBodyParameter(JavaParameter parameter) {
        JavaClass clazz = parameter.getJavaClass();
        List<ClassDesc> result = new ArrayList<>();
        getJavaClassDesc(clazz,result,isLimitParameter(parameter),"",new ArrayList<>());
        return result;
    }

    /**
     * 不考虑字段规则问题，获取类字段解释
     * @param javaClass
     * @return
     */
    @Override
    public List<ClassDesc> getJavaClassDescForResponer(JavaClass javaClass) {
        List<ClassDesc> result = new ArrayList<>();
        getJavaClassDesc(javaClass,result,false,"",new ArrayList<>());
        return result;
    }

    public void getJavaClassDescForHeadParameter(JavaClass clazz,List<ClassFieldDesc> container,List<JavaClass> aleradyResolve){

    }

    /**
     * 获取类描述
     * @param clazz 需描述的类
     * @param container 存放结果的容器
     * @param isLimit 是否生成类字段限制
     * @param groupName 限制组
     * @param aleradyResolve 以生成描述的类
     */
    public void getJavaClassDesc(JavaClass clazz, List<ClassDesc> container, boolean isLimit, String groupName, List<JavaClass> aleradyResolve) {
        //TODO Rule的分组需要加
        //TODO 小心json的别名！
        clazz = getRealClass(clazz);
        if (isAleradyResolve(clazz,aleradyResolve)){
            return;
        }
        aleradyResolve.add(clazz);
        ClassDesc classDesc = new ClassDesc();
        classDesc.setClassDesc(clazz.getComment());
        log.debug("开始抽取类{}的字段信息",clazz.getName());
        List<JavaField> fields =  clazz.getFields();
        List<ClassFieldDesc> fieldDescList = new ArrayList<>();
        classDesc.setClassFieldDescs(fieldDescList);
        for (JavaField field : fields) {
            if (!isMemberVar(field)){
                log.debug("字段{}非成员变量，跳过解析",field.getName());
                continue;
            }
            log.debug("开始遍历类{}的字段{}",clazz.getName(),field.getName());
           ClassFieldDesc desc =  getFieldDesc(field);
            //如果需要描述字段的限制规则
            if (isLimit){
                List<FieldRule> rule = getRule(field);
                desc.setFieldRule(rule);
            }
            //如果当前遍历的字段是非基本类型，也就是说，是Bean
            if (isBean(field.getType())){
                getJavaClassDesc(field.getType(),container,isLimit,groupName,aleradyResolve);
            }
            JavaClass innerClass = null;
            if ((innerClass = getCollectionBean(field))!=null ){
                getJavaClassDesc(innerClass,container,isLimit,groupName,aleradyResolve);
            }
            fieldDescList.add(desc);
        }
        if (isNeedGetSuperClassDesc(clazz)){
            getJavaClassDesc(clazz.getSuperJavaClass(),container,isLimit,groupName,aleradyResolve);
        }
    }
    private ClassFieldDesc getFieldDesc(JavaField field){
        ClassFieldDesc desc = new ClassFieldDesc();
        desc.setDataType(getDataType(field.getType()));
        desc.setParameterDesc(getParameterDesc(field));
        desc.setParameterName(field.getName());
        return desc;
    }

    private boolean isAleradyResolve(JavaClass clazz, List<JavaClass> aleradyResolve) {
        return aleradyResolve.contains(clazz);
    }

    private boolean isNeedGetSuperClassDesc(JavaClass javaClass){
        JavaClass superClass = javaClass.getSuperJavaClass();
        if (superClass == null){
            log.debug("类{}没有父类{}",javaClass.getName());
            return false;
        }
        log.debug("类{}有父类{}",javaClass.getName(),superClass.getName());
        if (superClass.isInterface()){
            log.debug("类{}的父类{}是接口类",javaClass.getName(),superClass.getName());
            return false;
        }
        if (isBean(superClass)){
            log.debug("类{}的父类{}是Bean，可以抽取类信息",javaClass.getName(),superClass.getName());
            return true;
        }
        log.debug("类{}的父类{}不是Bean，不可以抽取类信息",javaClass.getName(),superClass.getName());
        return false;

    }
    private boolean isMemberVar(JavaField field){
        return !field.isStatic();
    }

    /**
     * @param field
     * @return
     */
    private JavaClass getCollectionBean(JavaField field) {
        JavaClass javaClass = field.getType();
        if (JavaConstant.LIST_TPYE.equals(field.getType().getName())){
            DefaultJavaParameterizedType type = (DefaultJavaParameterizedType) javaClass;
            List<JavaType> types =  type.getActualTypeArguments();
            if (!CollectionUtils.isEmpty(types)){
                JavaClass innerType = (JavaClass) types.get(0);
                if (isBean(innerType)){
                    return innerType;
                }
            }
        }
        return null;
    }

    private JavaClass getRealClass(JavaClass clazz) {
        String className = clazz.getFullyQualifiedName();
        JavaClass javaClass = ClassContext.getClass(clazz.getFullyQualifiedName());

        if (javaClass == null){
            log.info("简单查找类失败，类名是{}",clazz.getFullyQualifiedName());
            //如果类名已经带逗号，则证明已经是全称，无需再反复查找
            if (className.indexOf(".")==-1){
                javaClass = tryGetRealClass(clazz);
            }
            if (javaClass == null){
                log.warn("上下文没有找到类的源码{}",clazz.getName());
                return clazz;
            }
        }
        return javaClass;
    }

    /**
     * 有时候，QODX返回的类名不是全称，这个时候，就要拿到声明它的类的import
     * 组装全称类名，然后去取JavaClass
     * @param javaClass
     * @return
     */
   private JavaClass tryGetRealClass(JavaClass javaClass) {
       String className = javaClass.getFullyQualifiedName();
       int index = 1;
       JavaClass result = null;
       while (true) {
           String fullName = fillClassName( javaClass, index++);
           log.info("第{}次反复查找，组装的类名全称是{}", index - 1, fullName);
           if (StringUtil.isEmpty(fullName)) {
               log.info("第{}次反复查找，找不到类{}的全称了", index - 1, className);
               break;
           }
           result = ClassContext.getClass(fullName);
           if (result != null) {
               log.info("第{}次反复查找，找到类{}的全称{}了", index - 1, className, fullName);
               break;
           }

       }
       return result;
   }

    /**
     * 补全类的全程
     * @param javaClass 该类所属的java类
     * @param i 击中数，当击中数和调用次数等同，则返回击中的值
     * @return
     */
    private String fillClassName(JavaClass javaClass, int i) {
        String className = javaClass.getFullyQualifiedName();
        List<String> imports = javaClass.getParent().getParentSource().getImports();
        String targerImport = "";
        int hit = 1;
        //首次调用该方法时，击中数为1，先用自身包名补全类全称
       if (hit ==i ){
           targerImport =  javaClass.getParent().getParentSource().getPackage().getName()+".";
       }else{
           hit=2;
           for (String importCode : imports) {
               if (importCode.indexOf("*")!=-1){
                   if (hit==i){
                       targerImport = importCode;
                       break;
                   }
                   hit++;
               }
           }
       }
        if (StringUtil.isEmpty(targerImport)){
            return null;
        }else{
            targerImport = getSimpleImport(targerImport);
            return targerImport+className;
        }

    }

    private String getSimpleImport(String targerImport) {
        int index = targerImport.indexOf("import");
        targerImport = targerImport.replace("import","");
        targerImport = targerImport.replace("*","");
        return targerImport.trim();
    }


    private String getLimitGroup(JavaParameter parameter){
       List<JavaAnnotation> annotations = parameter.getAnnotations();
        return null;
    }

    private List<FieldRule> getRule(JavaField field) {
        List<FieldRule> result = new ArrayList<>();
        List<JavaAnnotation> annotations = field.getAnnotations();
        //TODO 需要考虑group的可能性
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

    public String getParameterDesc(JavaField field) {
       String fieldDesc =  field.getComment();
       log.debug("字段名{}的描述是{}",field.getName(),fieldDesc);
        return fieldDesc;
    }
    @Override
    public DataType getDataType(JavaClass clazz) {
        String className  = AnnotationUtil.getSimpleClassName(clazz.getCanonicalName());
        if (JavaConstant.NUMBER_TYPES.contains(className)){
            return DataType.NUMBER;
        }
        if (JavaConstant.STRING_TPYE.equals(className)){
            return DataType.STRING;
        }
        if (JavaConstant.LIST_TPYE.equals(className)){
            return DataType.ARRAY;
        }
        return DataType.OBJECT;
    }

    @Override
    public boolean isExist(JavaClass javaClass) {
        logger.debug("判断类{}是否在给定的过滤集合中",javaClass.getFullyQualifiedName());
        String[] filters = config.getFilters();
        if (ArraysUtil.isEmpty(filters)){
            log.warn("没有配置过滤控制器的信息，将为所有的控制器生成接口文档");
            return true;
        }
        for (String filter : filters) {
           String className =  getClassName(filter);
           if (StringUtil.isEmpty(className)){
               continue;
           }
           if (className.equals(javaClass.getFullyQualifiedName())){
               logger.debug("类{}在给定的过滤集合中",javaClass.getFullyQualifiedName());
               return true;
           }
        }
        logger.debug("类{}不在给定的过滤集合中",javaClass.getFullyQualifiedName());
        return false;
    }

    /**
     * input :<pre>com.example.controller.ExampleControl.list</pre>
     * output:<pre>com.example.controller.ExampleControl</pre>
     * @param filter
     * @return
     */
    private String  getClassName(String filter) {
        int  lastIndex  = filter.lastIndexOf(".");
        String suffix = filter.substring(lastIndex+1,filter.length());
        if (StringUtil.isFirstUpperCase(suffix)){
            return filter;
        }else{
            String prefix = filter.substring(0,lastIndex);
            return prefix;
        }


    }


    @Override
    public boolean isInterfactClass(JavaClass javaClass) {
        logger.debug("开始遍历该文件的【{}】类",javaClass.getName());
        List<JavaAnnotation> annotations = javaClass.getAnnotations();
        for (JavaAnnotation javaAnnotation:annotations){
            String annotatianName  = javaAnnotation.getType().getCanonicalName();
            logger.debug("开始遍历该类【{}】的【{}】注解",javaClass.getName(),annotatianName);
            annotatianName = StringUtil.getClassNameForFullName(annotatianName);
            if (controllerService.isControlAnnotation(annotatianName)){
                logger.info("该类【{}】的注解【{}】是控制器注解",javaClass.getName(),annotatianName);
                return true;
            }
        }
        logger.debug("该类没有关键注解,故不是控制器类",javaClass.getName());
        return false;
    }

    @Override
    public List<ClassFieldDesc> getFieldDescForHeadParameter(JavaClass clazz){
        List<ClassFieldDesc> result = new ArrayList<>();
        getFieldDescForHeadParameter(clazz,result,new ArrayList<>(),"");
        return result;
    }

    /**
     * 获取类属性解释，为请求头参数设定
     * @param clazz 所需的类
     * @param container 容器
     * @param aleradyResolve 已解析的类
     * @param prefix 字段名前缀
     */
    public void getFieldDescForHeadParameter(JavaClass clazz,List<ClassFieldDesc> container,List<JavaClass> aleradyResolve,String prefix) {
        clazz = getRealClass(clazz);
        if (isAleradyResolve(clazz,aleradyResolve)){
            return;
        }
        aleradyResolve.add(clazz);
        ClassDesc classDesc = new ClassDesc();
        classDesc.setClassDesc(clazz.getComment());
        log.debug("开始抽取类{}的字段信息",clazz.getName());
        List<JavaField> fields =  clazz.getFields();
        List<ClassFieldDesc> fieldDescList = new ArrayList<>();
        classDesc.setClassFieldDescs(fieldDescList);
        for (JavaField field : fields) {
            if (!isMemberVar(field)){
                log.debug("字段{}非成员变量，跳过解析",field.getName());
                continue;
            }
            log.debug("开始遍历类{}的字段{}",clazz.getName(),field.getName());
            ClassFieldDesc desc = new ClassFieldDesc();
            desc.setDataType(getDataType(field.getType()));
            desc.setParameterDesc(getParameterDesc(field));
            desc.setParameterName(prefix+field.getName());
            //如果当前遍历的字段是非基本类型，也就是说，是Bean
            if (isBean(field.getType())){
                //TODO Spring MVC 下面的这个请求头怎么弄来着？
                getFieldDescForHeadParameter(field.getType(),container,aleradyResolve,field.getName()+".");
            }
        }
    }

    private boolean isBean(JavaClass javaClass){
        if (javaClass.isEnum()){
            return false;
        }
        if (javaClass.isArray()){
            return false;
        }
        String className = AnnotationUtil.getSimpleClassName(javaClass.getName());
        return !JavaConstant.OWN_TYPE.contains(className);
    }

    private boolean isLimitParameter(JavaParameter parameter) {
        List<JavaAnnotation> annotations = parameter.getAnnotations();
        return AnnotationUtil.isExistAnnotation("@Validated",annotations);
    }
}
