package com.cxh.uitl;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cxh.IO.TemplatedesignIO;
import com.cxh.model.Clazz;

public class JavaFileUitl extends TemplatedesignIO{
	
	private static Logger log = LogManager.getLogger(JavaFileUitl.class);
	
	private static JavaFileUitl uitl=new JavaFileUitl();
	private JavaFileUitl(){};
	public JavaFileUitl getUitl(){
		return uitl;
	}
	
	public static Clazz readFileToClazz(String filePath) {
		log.info("开始解析{}文件",filePath);
		try {
			return uitl.readFileToClazz(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			log.error("文件:{}不存在！",filePath);
			return null;
		} catch (IOException e) {
			log.error("解析文件:{}失败,发生IO错误",filePath,e);
			return null;
		}
		
	}
	@Override
	protected Clazz dealFile(InputStream in) throws IOException {
		String clazzStr = inputStreamConversionString(in,"utf-8");
		Clazz clazz = new Clazz();
		dealField(clazz,clazzStr);
		return clazz;
	}
	

	
	private void dealField(Clazz clazz,String clazzStr){
		String commentReg = "\\/\\*[\\s\\S]*?\\*\\/";
		String fieldReg= "private\\s+\\w+\\s+[a-z]\\w*?;";
		Pattern reg=Pattern.compile(commentReg+"\\s*?"+fieldReg);
		Matcher matcher=reg.matcher(clazzStr);
		while(matcher.find()){
			System.out.println(matcher.group());
		}
		
	}
	 /**
	    * 将字节流转换成字符串
	    * @param in 需要转换的字节流
	    * @param characterSet字符编码
	    * @return 转换完毕的字符串
	    * @throws IOException
	    */
	      public static String inputStreamConversionString(InputStream in,String characterSet) throws IOException{
			   BufferedReader read = new BufferedReader(new InputStreamReader(in,characterSet));
			   StringBuilder sb = new StringBuilder();
			   String line = "";
			   while((line = read.readLine())  !=  null){
				   sb.append(line);
			   }
			   return sb.toString();
	}
}
