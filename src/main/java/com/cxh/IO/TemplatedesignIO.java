package com.cxh.IO;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cxh.model.Clazz;
import com.cxh.uitl.JavaFileUitl;

public abstract  class TemplatedesignIO {
	
	private static Logger log = LogManager.getLogger(JavaFileUitl.class);
	
	public   Clazz readFileToClazz(InputStream in) throws IOException{
		try{
			return dealFile(in);
		}finally{
			try{
				if(in != null){
					in.close();
				}
			}catch(IOException e){
				log.error("关闭流失败,残念......",e);
			}
		}
	}
	
	protected abstract  Clazz dealFile(InputStream in) throws IOException;
	
	
	
}
