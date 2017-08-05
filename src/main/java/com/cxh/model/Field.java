package com.cxh.model;

/**
 * 字段类
 * @author Administrator
 *
 */
public class Field {
	private  String content;
	
	private String[] annotation;
	
	private String modifyMark;
	
	private String fieldType;
	
	private String fieldName;
	
	private String comment;

	/**
	 * 获取字段的属性名
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 设置字段的属性名
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * 得到整个字段的定义
	 * @return
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置整个字段的定义
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 得到字段注解
	 * @return
	 */
	public String[] getAnnotation() {
		return annotation;
	}
	/**
	 * 设置字段注解
	 * @param annotation
	 */
	public void setAnnotation(String[] annotation) {
		this.annotation = annotation;
	}
	/**
	 * 获取字段上的注释
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * 设置字段上的注释
	 * @return
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 得到字段的修饰符<br>
	 * eg:public static final
	 * @return
	 */
	public  String getModifyMark() {
		return modifyMark;
	}
	/**
	 * 设置字段的修饰符<br>
	 * eg:public static final
	 * @return
	 */
	public void setModifyMark(String modifyMark) {
		this.modifyMark = modifyMark;
	}
	
	/**
	 * 得到字段类型
	 * @return
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * 设置字段类型
	 * @param fieldType
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	

}
