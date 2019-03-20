package com.generatedoc.service.impl;

import com.generatedoc.config.ApplicationConfig;
import com.generatedoc.constant.JavaConstant;
import com.generatedoc.constant.ValidConstant;
import com.generatedoc.context.ClassContext;
import com.generatedoc.emnu.DataType;
import com.generatedoc.emnu.RuleType;
import com.generatedoc.exception.SystemException;
import com.generatedoc.model.ClassInfo;
import com.generatedoc.model.ClassFieldInfo;
import com.generatedoc.model.FieldRule;
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
import java.util.Optional;

@Service
public class QdoxClassServieImpl implements ClassService {
     public static final Logger log = LoggerFactory.getLogger(QdoxClassServieImpl.class);

     @Autowired
   private ControllerService controllerService;

     @Autowired
     ApplicationConfig config;

     @Override
    public List<ClassFieldInfo> getJavaClassDescForBodyParameter(JavaClass clazz, boolean isLimit, List<String> groupNames) {
        List<ClassFieldInfo> fieldDescList = getJavaClassDesc(clazz,isLimit,groupNames,new ArrayList<>());
        return fieldDescList;
    }


    /**
     * 不考虑字段规则问题，获取类字段解释
     * @param javaClass
     * @return
     */
    @Override
    public ClassInfo getJavaClassDescForResponer(JavaClass javaClass) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassDesc(null);
        classInfo.setDataType(getDataType(javaClass));
        List<ClassFieldInfo> fieldDescList = getJavaClassDesc(javaClass,false,new ArrayList<>(),new ArrayList<>());
        classInfo.setClassFieldInfos(fieldDescList);
        return classInfo;
    }

    public void getJavaClassDescForHeadParameter(JavaClass clazz, List<ClassFieldInfo> container, List<JavaClass> aleradyResolve){

    }

    /**
     * 获取类字段描述
     * @param clazz 需描述的类
     * @param isLimit 是否生成类字段限制
     * @param groupNames 限制组
     * @param aleradyResolve 以生成描述的类
     */
    public List<ClassFieldInfo> getJavaClassDesc(JavaClass clazz, boolean isLimit, List<String> groupNames, List<JavaClass> aleradyResolve) {
        //TODO 小心json的别名！
        clazz = getRealClass(clazz);
        if (isAleradyResolve(clazz,aleradyResolve)){
            return null;
        }
        aleradyResolve.add(clazz);
        log.debug("开始抽取类{}的字段信息",clazz.getName());
        List<JavaField> fields =  clazz.getFields();
        List<ClassFieldInfo> fieldDescList = new ArrayList<>();
        for (JavaField field : fields) {
            if (!isMemberVar(field)){
                log.debug("字段{}非成员变量，跳过解析",field.getName());
                continue;
            }
            log.debug("开始遍历类{}的字段{}",clazz.getName(),field.getName());
           ClassFieldInfo desc =  buildFieldDesc(field);
            //如果需要描述字段的限制规则
            if (isLimit){
                List<FieldRule> rule = getRule(field,groupNames);
                desc.setFieldRule(rule);
            }
            //如果当前遍历的字段是非基本类型，也就是说，是Bean
            if (isBean(field.getType())){
                ClassInfo nestingBean = new ClassInfo();
                nestingBean.setClassDesc(field.getName());
                List<ClassFieldInfo> fieldDescs = getJavaClassDesc(field.getType(),isLimit,groupNames,aleradyResolve);
                nestingBean.setClassFieldInfos(fieldDescs);
                nestingBean.setDataType(getDataType(field.getType()));
                desc.setNestingClssDesc(nestingBean);
            }else{
                //如果当前遍历的字段是集合类型
                desc.setNestingClssDesc(getNestingCollectionFieldDesc(field,isLimit,groupNames,aleradyResolve));
            }
            fieldDescList.add(desc);
        }
        if (isNeedGetSuperClassDesc(clazz)){
            List<ClassFieldInfo> superBeanField = getJavaClassDesc(clazz.getSuperJavaClass(),isLimit,groupNames,aleradyResolve);
            fieldDescList.addAll(superBeanField);
        }
        return fieldDescList;
    }
    private ClassFieldInfo buildFieldDesc(JavaField field){
        ClassFieldInfo desc = new ClassFieldInfo();
        desc.setDataType(getDataType(field.getType()));
        desc.setParameterDesc(getParameterDesc(field));
        desc.setParameterName(field.getName());
        return desc;
    }

    private ClassInfo getNestingCollectionFieldDesc(JavaField field, boolean isLimit, List<String> groupNames, List<JavaClass> aleradyResolve){
        JavaClass innerClass = null;
        if ((innerClass = getCollectionBean(field))!=null ){
            ClassInfo nestingBean = new ClassInfo();
            nestingBean.setClassDesc(field.getName());
            List<ClassFieldInfo> fieldDescs = getJavaClassDesc(innerClass,isLimit,groupNames,aleradyResolve);
            nestingBean.setClassFieldInfos(fieldDescs);
            nestingBean.setDataType(getDataType(innerClass));
            return nestingBean;
        }
        return null;
    }
    private boolean isAleradyResolve(JavaClass clazz, List<JavaClass> aleradyResolve) {
        return aleradyResolve.contains(clazz);
    }

    /**
     * 是否需要引入父类字段解释
     * @param javaClass
     * @return
     */
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
        if (JavaConstant.COLLECTION_TYPES.contains(field.getType().getName())){
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

    @Override
    public JavaClass getRealClass(JavaClass clazz) {
        String className = clazz.getCanonicalName();
        JavaClass javaClass = ClassContext.getClass(className);

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

    /**
     * 生成字段限制规则
     * @param field 字段
     * @param groupNames 控制器方法携带的限制组
     * @return
     */
    private List<FieldRule> getRule(JavaField field, List<String> groupNames) {
        List<FieldRule> result = new ArrayList<>();
        List<JavaAnnotation> annotations = field.getAnnotations();
        FieldRule fieldRule = buildRequired(annotations,groupNames);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
         fieldRule = buildMaxLimit(annotations,groupNames);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        fieldRule = buildMinLimit(annotations,groupNames);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        fieldRule = buildReg(annotations,groupNames);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        fieldRule = buildSize(annotations,groupNames);
        if (fieldRule != null) {
            result.add(fieldRule);
        }
        return result;
    }

    private FieldRule buildSize(List<JavaAnnotation> annotations, List<String> groupNames) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.SIZE_LIMIT,annotations);
        if (annotation == null || !isMatch(annotation,groupNames)){
            return null;
        }
        FieldRule rule = new FieldRule();
        Object min  = Optional.ofNullable(annotation.getNamedParameter(ValidConstant.MIN_PROPERTIES)).orElse(ValidConstant.DEFAULT_MIN);
        Object max  = Optional.ofNullable(annotation.getNamedParameter(ValidConstant.MAX_PROPERTIES)).orElse(ValidConstant.DEFAULT_MAX);
        rule.setRuleValue(String.format("最大不得超过%s位，最小不得超过%s位",max,min));
        rule.setRuleType(RuleType.SIZE_LENGTH_LIMIT);
        return rule;
    }

    private FieldRule buildRequired(List<JavaAnnotation> annotations, List<String> groupNames) {
        JavaAnnotation annotation = getRequiredAnnotation(annotations);
        if (annotation!=null && isMatch(annotation,groupNames)){
            FieldRule rule = new FieldRule();
            rule.setRuleType(RuleType.NOT_NULL);
            rule.setRuleValue("不可为空");
            return rule;
        }else{
            return null;
        }
    }

    /**
     * 判断校验注解是否生效
     * @param annotation
     * @param groupNames
     * @return
     */
    private boolean isMatch(JavaAnnotation annotation,List<String> groupNames){
        if (CollectionUtils.isEmpty(groupNames)){
            if (CollectionUtils.isEmpty(groupNames)){
                return true;
            }else{
                //如果字段校验注解无组限制，但是接口有，则不生效
                return false;
            }
        }
        List<String> groups = (List<String>) annotation.getNamedParameter("groups");
        if (CollectionUtils.isEmpty(groups)){
            return true;
        }
        for (String group : groups) {
            String fieldGroupName = AnnotationUtil.getSimpleClassName(group);
            for (String groupName : groupNames) {
                String parameterGroupName = AnnotationUtil.getSimpleClassName(group);
                if (fieldGroupName.equals(parameterGroupName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private FieldRule buildReg(List<JavaAnnotation> annotations, List<String> groupNames) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.REG,annotations);
        if (annotation == null || !isMatch(annotation,groupNames)){
            return null;
        }
        FieldRule fieldRule = new FieldRule();
        fieldRule.setRuleType(RuleType.REG);
        fieldRule.setRuleValue(annotation.getNamedParameter(ValidConstant.REG_VALUE)+"");
        return fieldRule;
    }

    private FieldRule buildMaxLimit(List<JavaAnnotation> annotations, List<String> groupNames) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.MAX_LIMIT,annotations);
        if (annotation == null || !isMatch(annotation,groupNames)){
            return null;
        }
        FieldRule rule = new FieldRule();
        String value  = annotation.getNamedParameter(ValidConstant.LENGTH_VALUE)+"";
        if (JavaConstant.INTEGER_MAX_NAME.equals(value)){
            value = JavaConstant.Integer_MAX_VALUE;
        }else if (JavaConstant.INTEGER_MIN_NAME .equals(value)) {
            value = JavaConstant.INTEGER_MIN_VALUE;
        }
        rule.setRuleValue(value);
        rule.setRuleType(RuleType.MAX_LENGTH_LIMIT);
        return rule;
    }

    private FieldRule buildMinLimit(List<JavaAnnotation> annotations, List<String> groupNames) {
        JavaAnnotation annotation = AnnotationUtil.getAnnotationByName(ValidConstant.MIN_LIMIT,annotations);
        if (annotation == null || !isMatch(annotation,groupNames)){
            return null;
        }
        FieldRule rule = new FieldRule();
        Long value  = Long.valueOf(annotation.getNamedParameter(ValidConstant.LENGTH_VALUE)+"");
        rule.setRuleValue(value+"");
        rule.setRuleType(RuleType.MIN_LENGTH_LIMIT);
        return rule;
    }

    private boolean isLengthLimit(List<JavaAnnotation> annotations) {
        return AnnotationUtil.isExistAnnotation(ValidConstant.MAX_LIMIT,annotations) ||
                AnnotationUtil.isExistAnnotation(ValidConstant.MIN_LIMIT,annotations);
    }

    private JavaAnnotation getRequiredAnnotation(List<JavaAnnotation> annotations) {
        JavaAnnotation annotation =  AnnotationUtil.getAnnotationByName("NotNull",annotations);
        if (annotation != null) {
            return annotation;
        }
         annotation =  AnnotationUtil.getAnnotationByName("NotBlank",annotations);
        if (annotation != null) {
            return annotation;
        }
         annotation =  AnnotationUtil.getAnnotationByName("NotEmpty",annotations);
        if (annotation != null) {
            return annotation;
        }
        return null;
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
        if (JavaConstant.BOOLEAN_BASE_TYPE.endsWith(className)){
            return DataType.BOOLEAN;
        }
        if (JavaConstant.COLLECTION_TYPES.contains(className) || getRealClass(clazz).isArray()){
            return DataType.ARRAY;
        }
        if (getRealClass(clazz).isEnum()){
            return DataType.ENUM;
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
    public List<ClassFieldInfo> getFieldDescForHeadParameter(JavaClass clazz){
        List<ClassFieldInfo> result = new ArrayList<>();
        getFieldDescForHeadParameter(clazz,result,new ArrayList<>(),"");
        return result;
    }

    /**
     * 获取类的描述，作为标题
     * @param javaClass
     * @return
     */
    @Override
    public String buildTitle(JavaClass javaClass) {
        DocletTag docletTag  = javaClass.getTagByName("Title");
        String title = "";
        if (docletTag == null){
            log.info("找不到该类{}的Title注释信息",javaClass.getName());
            title =  javaClass.getComment();
        }else{
            title =  docletTag.getValue();
        }
        if (StringUtil.isEmpty(title)){
            log.warn("类名{}找不到有效的接口文档描述，取类名为标题");
            title = javaClass.getName();
        }
        return title;
    }

    @Override
    public String toJsonString(ClassInfo classInfo) {
        if (classInfo == null){
            return null;
        }
       StringBuilder write = new StringBuilder();
       DataType dataType = classInfo.getDataType();
        if (dataType == null) {
            log.warn("参数{}描述的的参数数据类型为空，生成Json示例参数失败");
            throw new SystemException("生成接口文档失败");
        }
        if (dataType.equals(DataType.ARRAY)) {
            writeArray(classInfo,write);
        }else if (dataType.equals(DataType.OBJECT)){
            writeField(classInfo.getClassFieldInfos(),write);
        }
        return write.toString();
    }
    private void  writeArray(ClassInfo desc, StringBuilder write){
        write.append("[");
            writeField(desc.getClassFieldInfos(),write);
        write.append("]");
    }
    private void writeField(List<ClassFieldInfo> fieldDescList, StringBuilder write){
        write.append("{");
            if (!CollectionUtils.isEmpty(fieldDescList)){
                for (ClassFieldInfo classFieldInfo : fieldDescList) {
                    writeKey(classFieldInfo,write);
                    writeValue(classFieldInfo,write);
                }
                //删除最后的逗号
                write.deleteCharAt(write.length()-1);
            }
        write.append("}");
    }

    private void writeValue(ClassFieldInfo classFieldInfo, StringBuilder write) {
        DataType dataType = classFieldInfo.getDataType();
        if (dataType == null) {
            log.warn("字段{}的数据类型为空，无法转成json_value", classFieldInfo.getParameterName());
            write.append("\"数据异常\"");
        }else {
            switch (dataType){
                case BOOLEAN:  write.append("false"); break;
                case STRING: write.append("\"字符串\"");break;
                case ARRAY: writeArray(classFieldInfo,write);break;
                case NUMBER: write.append("1234");;break;
                case OBJECT: writeBean(classFieldInfo,write); break;
                case ENUM:write.append("\"待补充\""); break;
                default:write.append("\"未知数据类型\"");
            }
        }
        write.append(",");
    }

    private void writeBean(ClassFieldInfo field, StringBuilder write) {
        ClassInfo classInfo = field.getNestingClssDesc();
        if (classInfo !=null && CollectionUtils.isNotEmpty(classInfo.getClassFieldInfos())){
            writeField(classInfo.getClassFieldInfos(),write);
        }else{
            write.append("{}");
        }
    }

    private void writeArray(ClassFieldInfo field, StringBuilder write){
        write.append("[");
             //TODO 数组内也可能是数字，字符串什么的
            ClassInfo classInfo = field.getNestingClssDesc();
            if (classInfo !=null && CollectionUtils.isNotEmpty(classInfo.getClassFieldInfos())){
                writeField(classInfo.getClassFieldInfos(),write);
            }
        write.append("]");
    }

    private void writeKey(ClassFieldInfo classFieldInfo, StringBuilder write){
        write.append("\""+ classFieldInfo.getParameterName() +"\":");      //eg: "name":
    }

    /**
     * 获取类属性解释，为请求头参数设定
     * @param clazz 所需的类
     * @param container 容器
     * @param aleradyResolve 已解析的类
     * @param prefix 字段名前缀
     */
    public void getFieldDescForHeadParameter(JavaClass clazz, List<ClassFieldInfo> container, List<JavaClass> aleradyResolve, String prefix) {
        clazz = getRealClass(clazz);
        if (isAleradyResolve(clazz,aleradyResolve)){
            return;
        }
        aleradyResolve.add(clazz);
        log.debug("开始抽取类{}的字段信息",clazz.getName());
        List<JavaField> fields =  clazz.getFields();
        for (JavaField field : fields) {
            if (!isMemberVar(field)){
                log.debug("字段{}非成员变量，跳过解析",field.getName());
                continue;
            }
            log.debug("开始遍历类{}的字段{}",clazz.getName(),field.getName());
            ClassFieldInfo desc = new ClassFieldInfo();
            desc.setDataType(getDataType(field.getType()));
            desc.setParameterDesc(getParameterDesc(field));
            desc.setParameterName(prefix+field.getName());
            //如果当前遍历的字段是非基本类型，也就是说，是Bean
            if (isBean(field.getType())){
                //TODO Spring MVC 下面的这个请求头怎么弄来着？
                getFieldDescForHeadParameter(field.getType(),container,aleradyResolve,field.getName()+".");
            }else{
                container.add(desc);
            }
        }
    }

    private boolean isBean(JavaClass javaClass){
        if (isBaseType(javaClass)){
            return false;
        }
        if (javaClass.isEnum()){
            return false;
        }
        if (javaClass.isArray()){
            return false;
        }
        String className = AnnotationUtil.getSimpleClassName(javaClass.getName());
        return !JavaConstant.OWN_TYPE.contains(className);
    }

    private boolean isBaseType(JavaClass javaClass) {
        String name = javaClass.getName();
        return JavaConstant.BASE_TYPE.contains(name);
    }


}
