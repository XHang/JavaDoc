package com.generatedoc.service.impl;

import com.generatedoc.constant.MappingConstant;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.entity.MarkDownTableDto;
import com.generatedoc.entity.ParameterDesc;
import com.generatedoc.service.MarkDownMethodService;
import com.generatedoc.util.MarkdownUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MarkDownMethodServiceImpl implements MarkDownMethodService {
    @Override
    public void buildMethodDoc(ApiInterface apiInterface, StringBuilder sb, int index) {
        buildTitle(apiInterface,sb,index);
        sb.append(MarkdownUtil.buildSingleOrderItem(1,"编写人员："+apiInterface.getAuthor()));
        sb.append(MarkdownUtil.buildSingleOrderItem(2,"功能: "+apiInterface.getDesc()));
        sb.append(MarkdownUtil.buildSingleOrderItem(3,"调用方式: "+apiInterface.getRequestType()));
        sb.append(MarkdownUtil.buildSingleOrderItem(4,"调用URL: "+apiInterface.getUrl()));
        sb.append(MarkdownUtil.buildSingleOrderItem(5,"参数说明"));
        buildRequestParameter(sb,apiInterface);

    }

    private void buildRequestParameter(StringBuilder sb, ApiInterface apiInterface) {
        List<ParameterDesc> list =  apiInterface.getParameters();
        if (CollectionUtils.isEmpty(list)){
            sb.append(MarkdownUtil.buildText("无"));
        }
        MarkDownTableDto dto = parameterDescToMarkDownTableDto(list);
        sb.append(MarkdownUtil.buildTable(dto));
    }

    private MarkDownTableDto parameterDescToMarkDownTableDto(List<ParameterDesc> list) {
        //TODO 
        MarkDownTableDto dto = new MarkDownTableDto();
        List<String> column =  new ArrayList<>();
        List<List<String>> container = new ArrayList<>();
        //buildParameterNameColumn(column,list);
        column.add("参数名");
        column.add("参数解释");
        column.add("参数类型");
        return dto;
    }

    private void buildParameterNameColumn(List<String> column, List<ParameterDesc> list,List<List<String>> container) {
        column.add("参数名");
        for (int index = 0; index < list.size(); index++) {
            ParameterDesc desc = list.get(0);
            String parameterName = desc.getParameterName();

        }
    }

    private void buildTitle(ApiInterface apiInterface, StringBuilder sb, int index) {
        String titleIndex = MappingConstant.numberConverter(index);
        String title =titleIndex+":"+apiInterface.getDesc();
        sb.append(MarkdownUtil.buildTitle(2,title));
    }
}
