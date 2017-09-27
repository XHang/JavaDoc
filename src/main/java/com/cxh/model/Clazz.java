package com.cxh.model;


import java.util.List;

import com.cxh.Enum.ClassType;

/**
 * 解析java文件生成的实体类对象
 * @author Administrator
 *
 */
public class Clazz {
	private String filePath;
	
	private String clazzName;
	
	private String[] clazzAnnotation;
	
	private String staticCodeBlock;
	
	private String constructCodeBlock;
	
	private Method[] methods;
	
	private List<Field>  fields;
	
	private Clazz[] lnternalClass;
	
	private String content;
	
	/**
	 * 获取该类文件的绝对路径
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * 设置该类文件的绝对路径
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * 获取该类的内部类列表
	 * @return
	 */
	public Clazz[] getLnternalClass() {
		return lnternalClass;
	}
	/**
	 * 设置该类的内部类列表
	 * @param lnternalClass
	 */
	public void setLnternalClass(Clazz[] lnternalClass) {
		this.lnternalClass = lnternalClass;
	}
	/**
	 * 获取该类的内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置该类的内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 得到该类的类名
	 * @return
	 */
	public String getClazzName() {
		return clazzName;
	}
	/**
	 * 设置该类的类名
	 * @param clazzName
	 */
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	/**
	 * 得到该类的静态代码块
	 * @return
	 */
	public String getStaticCodeBlock() {
		return staticCodeBlock;
	}
	/**
	 * 设置该类的静态代码块
	 * @param staticCodeBlock
	 */
	public void setStaticCodeBlock(String staticCodeBlock) {
		this.staticCodeBlock = staticCodeBlock;
	}
	/**
	 * 得到该类的构造代码块
	 */
	public String getConstructCodeBlock() {
		return constructCodeBlock;
	}
	/**
	 * 设置该类的构造代码块
	 * @param staticCodeBlock
	 */
	public void setConstructCodeBlock(String constructCodeBlock) {
		this.constructCodeBlock = constructCodeBlock;
	}
	/**
	 * 得到该类的方法合集
	 * @return
	 */
	public Method[] getMethods() {
		return methods;
	}
	/**
	 * 设置该类的方法合集
	 * @param methods
	 */
	public void setMethods(Method[] methods) {
		this.methods = methods;
	}
	
	/**
	 * 得到该类的字段集合
	 * @return
	 */
	public List<Field> getFields() {
		return fields;
	}
	/**
	 * 设置该类的字段集合
	 * @param fields
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	/**
	 * 获取该类的类型（接口？枚举？）
	 * @return
	 */
	public ClassType getType() {
		return type;
	}
	/**
	 * 设置该类的类型（接口？枚举？）
	 * @return
	 */
	public void setType(ClassType type) {
		this.type = type;
	}
	/**
	 * 得到包名
	 * @return
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * 设置包名
	 * @return
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * 得到类的import列表
	 * @return
	 */
	public String[] getImportList() {
		return importList;
	}
	/**
	 * 设置该类的import列表
	 * @param importList
	 */
	public void setImportList(String[] importList) {
		this.importList = importList;
	}
	/**
	 * 得到该类的注解
	 * @return
	 */
	
	public String[] getClazzAnnotation() {
		return clazzAnnotation;
	}
	/**
	 * 设置该类的注解
	 * @param clazzAnnotation
	 */
	public void setClazzAnnotation(String[] clazzAnnotation) {
		this.clazzAnnotation = clazzAnnotation;
	}

	private ClassType type;
	
	private String packageName;
	
	private String[] importList;
	
	
}
