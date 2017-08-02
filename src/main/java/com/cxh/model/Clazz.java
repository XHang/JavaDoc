package com.cxh.model;

import java.util.List;

/**
 * 解析java文件生成的实体类对象
 * @author Administrator
 *
 */
public class Clazz {
	private String filePath;
	
	private String clazzName;
	
	private String clazzAnnotation;
	
	private String staticCodeBlock;
	
	private String constructCodeBlock;
	
	private Method[] methods;
	
	private Field[]  fields;
	
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
	 * @return
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
	 * @return
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
	 * @return
	 */
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	/**
	 * 设置该类的类名
	 * @return
	 */
	public String getClazzAnnotation() {
		return clazzAnnotation;
	}

	public void setClazzAnnotation(String clazzAnnotation) {
		this.clazzAnnotation = clazzAnnotation;
	}

	public String getStaticCodeBlock() {
		return staticCodeBlock;
	}

	public void setStaticCodeBlock(String staticCodeBlock) {
		this.staticCodeBlock = staticCodeBlock;
	}

	public String getConstructCodeBlock() {
		return constructCodeBlock;
	}

	public void setConstructCodeBlock(String constructCodeBlock) {
		this.constructCodeBlock = constructCodeBlock;
	}

	public Method[] getMethods() {
		return methods;
	}

	public void setMethods(Method[] methods) {
		this.methods = methods;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public ClassType getType() {
		return type;
	}

	public void setType(ClassType type) {
		this.type = type;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String[] getImportList() {
		return importList;
	}

	public void setImportList(String[] importList) {
		this.importList = importList;
	}

	private ClassType type;
	
	private String packageName;
	
	private String[] importList;
	
	
}
