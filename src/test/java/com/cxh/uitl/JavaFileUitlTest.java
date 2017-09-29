package com.cxh.uitl;

import java.io.IOException;

import org.junit.Test;

public class JavaFileUitlTest {
	@Test
	public void testReadFileToClazz() throws IOException{
		String filepath ="C:\\Users\\Administrator\\git\\JavaDoc\\src\\main\\resources\\Test.java";
		JavaFileUitl.readFileToClazz(filepath);
	}
}
