package com.generatedoc.util;

import org.junit.Test;

/**
 * 测试MarkDown文件的生成情况
 */
public class MarkDownUtilTest {


    @Test
    public void buildMarkDownFile(){
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(1,"测试接口文档"));
        sb.append(MarkdownUtil.buildTitle(2,"用户添加接口"));
        sb.append(MarkdownUtil.buildSingleOrderItem(1,"编写人员：cxh"));
        sb.append(MarkdownUtil.buildReferenceBlock("其实未必是她写的。。"));
        sb.append(MarkdownUtil.buildSingleOrderItem(2,"添加一个用户，就这么简单，不然你还想多复杂？"));
        sb.append(MarkdownUtil.buildSingleOrderItem(2,"调用方式：get"));


    }
}
