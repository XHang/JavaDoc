package com.cxh.uitl;

import java.io.IOException;

import org.junit.Test;

public class JavaFileUitlTest {
	@Test
	public void testReadFileToClazz() throws IOException{
		String filepath ="D:\\dsspplatform\\workspace\\cxh_defloss\\defloss\\server\\src\\main\\java\\com\\dstech\\dssp\\defloss\\bean\\PrpLcomponent.java";
		JavaFileUitl.readFileToClazz(filepath);
	}
}
