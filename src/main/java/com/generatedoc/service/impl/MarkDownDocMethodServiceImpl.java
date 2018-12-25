package com.generatedoc.service.impl;

import com.generatedoc.constant.MappingConstant;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.entity.MarkDownTableDto;
import com.generatedoc.entity.ParameterDesc;
import com.generatedoc.entity.ReturnFieldDesc;
import com.generatedoc.service.MarkDownDocMethodService;
import com.generatedoc.util.DateUitl;
import com.generatedoc.util.MarkdownUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkDownDocMethodServiceImpl implements MarkDownDocMethodService {
     public static final Logger log = LoggerFactory.getLogger(MarkDownDocMethodServiceImpl.class);
    @Override
    public void buildMethodDoc(ApiInterface apiInterface, StringBuilder sb, int index) {
        log.debug("开始生成方法{}的接口文档",apiInterface.getDesc());
        buildTitle(apiInterface,sb,index);
        sb.append(MarkdownUtil.buildSingleOrderItem(1,"编写人员："+apiInterface.getAuthor()));
        sb.append(MarkdownUtil.buildSingleOrderItem(2,"功能: "+apiInterface.getDesc()));
        sb.append(MarkdownUtil.buildSingleOrderItem(3,"调用方式: "+apiInterface.getRequestType()));
        sb.append(MarkdownUtil.buildSingleOrderItem(4,"调用URL: "+MarkdownUtil.buildCodeLine(apiInterface.getUrl())));
        buildRequestArea(sb,apiInterface);
        buildResponseArea(sb,apiInterface);
        sb.append(MarkdownUtil.buildSingleOrderItem(7,"修订日期 "));
        sb.append(MarkdownUtil.buildText(DateUitl.formatterChinaDate(LocalDateTime.now())));
        sb.append(MarkdownUtil.buildSingleOrderItem(8,"其他说明 "));
    }

    private void buildResponseArea(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildSingleOrderItem(6,"响应示例"));
        buildResponseDesc(sb,apiInterface);
        buildResponseExample(sb,apiInterface);
    }

    private void buildResponseExample(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildText("**响应示例**"));
        sb.append(MarkdownUtil.buildText("暂无"));
        //TODO 未完成
    }

    private void buildResponseDesc(StringBuilder sb, ApiInterface apiInterface) {
        List<ReturnFieldDesc> returnFieldDescs = apiInterface.getReturnFieldDesc();
        if (CollectionUtils.isEmpty(returnFieldDescs)){
            sb.append(MarkdownUtil.buildText("无"));
            return;
        }
        List<String> column = new ArrayList<>();
        column.add("字段名");
        column.add("字段解释");
        column.add("字段类型");
        MarkDownTableDto dto = new MarkDownTableDto();
        dto.setColumns(column);
        List<List<String>> data = new ArrayList<>();
        for (ReturnFieldDesc returnFieldDesc : returnFieldDescs) {
            log.debug("生成响应参数{}的解释",returnFieldDesc.getParameterName());
            List<String> row = new ArrayList<>();
            row.add(returnFieldDesc.getParameterName());
            row.add(returnFieldDesc.getParameterDesc());
            row.add(returnFieldDesc.getDataType().getName());
            data.add(row);
        }
        dto.setData(data);
        sb.append(MarkdownUtil.buildTable(dto));
    }

    private void buildRequestExample(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildText("**调用示例**"));
        sb.append(MarkdownUtil.buildText("暂无"));
        //TODO 暂缓开发
    }

    private void buildRequestArea(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildSingleOrderItem(5,"参数说明"));
        buildRequestParameterDesc(sb, apiInterface);
        buildRequestExample(sb,apiInterface);
    }
    private void buildRequestParameterDesc(StringBuilder sb, ApiInterface apiInterface){
        List<ParameterDesc> list =  apiInterface.getParameters();
        if (CollectionUtils.isEmpty(list)){
            sb.append(MarkdownUtil.buildText("无"));
            return;
        }
        MarkDownTableDto dto = parameterDescToMarkDownTableDto(list);
        sb.append(MarkdownUtil.buildTable(dto));
    }

    private MarkDownTableDto parameterDescToMarkDownTableDto(List<ParameterDesc> list) {
        MarkDownTableDto dto = new MarkDownTableDto();
        List<String> column =  new ArrayList<>();
        List<List<String>> container = new ArrayList<>();
        column.add("参数名");
        column.add("参数解释");
        column.add("参数类型");
        column.add("限制规则");
        column.add("默认值");
        List<List<String>> data = new ArrayList<>();
        for (ParameterDesc desc : list) {
            log.debug("生成请求参数{}的解释",desc.getParameterName());
            List<String> row = new ArrayList<>();
            row.add(desc.getParameterName());
            row.add(desc.getParameterDesc());
            row.add(desc.getDataType().getName());
            row.add("");
            row.add(desc.getDefaultValue());
            data.add(row);
        }
        dto.setColumns(column);
        dto.setData(data);
        return dto;
    }



    private void buildTitle(ApiInterface apiInterface, StringBuilder sb, int index) {
        String titleIndex = MappingConstant.numberConverter(index);
        String title =titleIndex+":"+apiInterface.getDesc();
        sb.append(MarkdownUtil.buildTitle(2,title));
    }
}
