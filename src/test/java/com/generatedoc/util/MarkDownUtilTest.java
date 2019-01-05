package com.generatedoc.util;

import com.generatedoc.emnu.RequestType;
import com.generatedoc.model.ApiInterface;
import com.generatedoc.model.MarkDownTableDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 测试MarkDown文件的生成情况
 */
public class MarkDownUtilTest {


    //@Test
    public void buildMarkDownFile(){
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(1,"测试接口文档"));
        sb.append(MarkdownUtil.buildTitle(2,"用户添加接口"));
        sb.append(MarkdownUtil.buildSingleOrderItem(1,"编写人员：cxh"));
        sb.append(MarkdownUtil.buildReferenceBlock("其实未必是她写的。。"));
        sb.append(MarkdownUtil.buildSingleOrderItem(2,"添加一个用户，就这么简单，不然你还想多复杂？"));
        sb.append(MarkdownUtil.buildSingleOrderItem(2,"调用方式：get"));
        buildTable(sb);
        buildCode(sb);
        IOUtil.saveFile(sb.toString(),"d://test.md");
        System.out.println("saveSuccess");
    }

    private void buildTable(StringBuilder stringBuilder){
        List<String> column = Arrays.asList("字段名","解释","是否必填","默认值");
        List<String> data1 = Arrays.asList("field1","字段1","否","无");
        List<String> data2 = Arrays.asList("field2","字段2","否","无");
        List<String> data3 = Arrays.asList("field3","字段4","否","无");
        List<String> data4 = Arrays.asList("field4","字段4","否","4");
        List<List<String>> data = Arrays.asList(data1,data2,data3,data4);
        MarkDownTableDto dto = new MarkDownTableDto();
        dto.setColumns(column);
        dto.setData(data);
        String table = MarkdownUtil.buildTable(dto);
        stringBuilder.append(table);
    }

    private void buildCode(StringBuilder sb ){
        ApiInterface apiInterface = new ApiInterface();
        apiInterface.setAuthor("cxh");
        apiInterface.setDesc("json代码测试");
        apiInterface.setRequestType(RequestType.GET);
        apiInterface.setTime(LocalDateTime.now());
        String json  =  JSONUtil.toJSONString(apiInterface);
        String formatJson = JsonUtil.jsonFormat(json);
        String code = MarkdownUtil.buildCodeArea(formatJson,"json");
        sb.append(code);
    }
}
