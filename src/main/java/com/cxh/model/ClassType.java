package com.cxh.model;

/**
 * 解析的java文件类别
 * @author Administrator
 *
 */
public enum ClassType {
	/**
	 * 普通的类
	 */
	CLASS,
	/**
	 * 接口类
	 */
	INTERFACE,
	/**
	 * 抽象类
	 */
	ABSTRACT_CLASS,
	/**
	 * 注解类
	 */
	ANNOTATION_CLASS,
	/**
	 * 枚举类
	 */
	ENUM_CLASS;
}
