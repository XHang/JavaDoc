package com.cxh.uitl;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cxh.IO.TemplatedesignIO;
import com.cxh.model.Clazz;

public class JavaFileUitl extends TemplatedesignIO{
	
	private static Logger log = LogManager.getLogger(JavaFileUitl.class);
	
	private static JavaFileUitl uitl;
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
		return null;
	}
}
