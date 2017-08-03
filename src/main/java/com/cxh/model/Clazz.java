package com.cxh.model;


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
	 * 得到
	 * @return
	 */
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
