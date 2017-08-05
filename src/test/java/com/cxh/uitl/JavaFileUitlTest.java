package com.cxh.uitl;

import java.io.IOException;

import org.junit.Test;

public class JavaFileUitlTest {
	@Test
	public void testReadFileToClazz() throws IOException{
		String filepath ="D:\\DSSPWorkSpace\\HttpClient\\src\\main\\java\\com\\test\\HttpClitntTest.java";
		JavaFileUitl.readFileToClazz(filepath);
	}
}
