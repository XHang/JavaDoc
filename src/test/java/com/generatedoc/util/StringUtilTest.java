package com.generatedoc.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void isEmptyTest(){
        String str = null;
        String str1 = "";
        String str2 = "    ";
        String str3 = "sdgfrd";
        Assert.assertTrue(StringUtil.isEmpty(str));
        Assert.assertTrue(StringUtil.isEmpty(str1));
        Assert.assertTrue(StringUtil.isEmpty(str2));
        Assert.assertFalse(StringUtil.isEmpty(str3));
    }



    @Test
    public void getClassNameForFullName() {
        String fullName = "com.oracle.deploy.update.UpdateCheck";
        String result = StringUtil.getClassNameForFullName(fullName);
        System.out.println("完全限定名["+fullName+"]取出简称为："+result);
    }

}
