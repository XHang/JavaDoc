package com.cxh.model;

import java.util.Map;

/**
 * 方法
 * @author Administrator
 *
 */
public class Method {
	private String content;
	
	private String returnType;
	
	private String motifyMark;
	
	private Map<Object,String> parameterMap;
	
	private String comment;
	
	private String[] annotations ;

	/**
	 * 得到该方法的注释
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * 设置该方法的注释
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * 得到该方法的注解
	 * @return
	 */
	public String[] getAnnotations() {
		return annotations;
	}
	/**
	 * 设置该方法的注解
	 * @return
	 */
	public void setAnnotations(String[] annotations) {
		this.annotations = annotations;
	}

	/**
	 * 得到该方法的所有内容
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置该方法的所有内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 得到方法的返回值类型
	 * @return
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * 设置方法的返回值类型
	 * @param returnType
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	/**
	 * 得到方法的修饰符
	 * @return
	 */
	public String getMotifyMark() {
		return motifyMark;
	}
   /**
    * 设置方法的修饰符
    * @param motifyMark
    */
	public void setMotifyMark(String motifyMark) {
		this.motifyMark = motifyMark;
	}
	/**
	 * 得到方法的形式参数<br/>
	 * key是参数类型<br/>
	 * value是参数值
	 * @return
	 */
	public Map<Object, String> getParameterMap() {
		return parameterMap;
	}
	/**
	 * 设置方法的形式参数<br/>
	 * key是参数类型<br/>
	 * value是参数值
	 * @return
	 */
	public void setParameterMap(Map<Object, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

}
