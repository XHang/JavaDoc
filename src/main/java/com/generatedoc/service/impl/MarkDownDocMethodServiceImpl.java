package com.generatedoc.service.impl;

import com.generatedoc.constant.MappingConstant;
import com.generatedoc.emnu.RequestType;
import com.generatedoc.entity.*;
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
import java.util.Optional;

@Service
public class MarkDownDocMethodServiceImpl implements MarkDownDocMethodService {
     public static final Logger log = LoggerFactory.getLogger(MarkDownDocMethodServiceImpl.class);
    @Override
    public void buildMethodDoc(ApiInterface apiInterface, StringBuilder sb, int index) {
        log.debug("开始生成方法{}的接口文档",apiInterface.getDesc());
        buildTitle(apiInterface,sb,index);
        sb.append(writeAuthor(apiInterface.getAuthor()));
        sb.append(writeMethodDesc(apiInterface.getDesc()));
        sb.append(writeRequestType(apiInterface.getRequestType()));
        sb.append(writeRequestUrl(apiInterface.getUrl()));
        buildRequestArea(sb,apiInterface);
        buildResponseArea(sb,apiInterface);
        sb.append( writeCreateTime());
        sb.append( buildOtherDesc());
    }

    private String buildOtherDesc() {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(3,"其他说明:"));
        sb.append(MarkdownUtil.buildText("  "));
        return sb.toString();
    }

    private String writeCreateTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(3,"修订日期:"));
        sb.append(MarkdownUtil.buildText(DateUitl.formatterChinaDate(LocalDateTime.now())));
        return sb.toString();
    }

    private String writeRequestUrl(String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(3,"调用URL:"));
        sb.append(MarkdownUtil.buildText(url));
        return sb.toString();
    }

    private String writeRequestType(RequestType requestType) {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(3,"调用方式:"));
        sb.append(MarkdownUtil.buildText(requestType.name()));
        return sb.toString();
    }

    private String writeMethodDesc(String desc) {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(3,"功能:"));
        sb.append(MarkdownUtil.buildText(desc));
        return sb.toString();
    }

    private String  writeAuthor(String author) {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(3,"编写人员："));
        sb.append(MarkdownUtil.buildText(author));
        return sb.toString();
    }

    private void buildResponseArea(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildTitle(3,"响应解释"));
        buildResponseDesc(sb,apiInterface);
        buildResponseExample(sb,apiInterface);
    }

    private void buildResponseExample(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildText("**响应示例**"));
        sb.append(MarkdownUtil.buildText("暂无"));
        //TODO 未完成
    }

    private void buildResponseDesc(StringBuilder sb, ApiInterface apiInterface) {
        List<ClassDesc> responseDescList = apiInterface.getResponseDesc();
        if (CollectionUtils.isEmpty(responseDescList)){
            sb.append(MarkdownUtil.buildText("无"));
            return;
        }
        for (ClassDesc responseDesc : responseDescList) {
            sb.append(MarkdownUtil.buildBoldText(responseDesc.getClassDesc()+"字段解释"));
            writeResponseFieldDesc(sb,responseDesc.getClassFieldDescs());
        }
    }
    private void writeResponseFieldDesc(StringBuilder write,List<ClassFieldDesc> fields){
        List<String> column = new ArrayList<>();
        column.add("字段名");
        column.add("字段解释");
        column.add("字段类型");
        MarkDownTableDto dto = new MarkDownTableDto();
        dto.setColumns(column);
        List<List<String>> data = new ArrayList<>();
        for (ClassFieldDesc field : fields) {
            log.debug("生成响应参数{}的解释",field.getParameterName());
            List<String> row = new ArrayList<>();
            row.add(field.getParameterName());
            row.add(field.getParameterDesc());
            row.add(field.getDataType().getName());
            data.add(row);
        }
        dto.setData(data);
        write.append(MarkdownUtil.buildTable(dto));
    }

    private void buildRequestExample(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildText("**调用示例**"));
        sb.append(MarkdownUtil.buildText("暂无"));
        //TODO 暂缓开发
    }

    private void buildRequestArea(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildTitle(3,"参数说明"));
        buildHeadParameterDesc(sb, apiInterface.getHeaderParameters());
        buildBodyParameterDesc(sb, apiInterface.getBodyParameters());
        buildRequestExample(sb,apiInterface);
    }

    private void buildBodyParameterDesc(StringBuilder sb, List<ClassDesc> classDescList) {
        sb.append(MarkdownUtil.buildBoldText("请求头参数"));
        if (CollectionUtils.isEmpty(classDescList)){
            sb.append(MarkdownUtil.buildText("无"));
            return;
        }
        for (ClassDesc classDesc : classDescList) {
            sb.append(MarkdownUtil.buildBoldText(classDesc.getClassDesc()+"字段解释"));
            MarkDownTableDto dto = parameterDescToMarkDownTableDto(classDesc.getClassFieldDescs());
        }
    }

    private void buildHeadParameterDesc(StringBuilder sb, List<HeadParameterDesc> list){
        sb.append(MarkdownUtil.buildBoldText("请求头参数"));
        if (CollectionUtils.isEmpty(list)){
            sb.append(MarkdownUtil.buildText("无"));
            return;
        }
        MarkDownTableDto dto = parameterDescToMarkDownTableDto(list);
        sb.append(MarkdownUtil.buildTable(dto));
    }

    private MarkDownTableDto parameterDescToMarkDownTableDto(List<? extends  ClassFieldDesc> list) {
        MarkDownTableDto dto = new MarkDownTableDto();
        List<String> column =  new ArrayList<>();
        List<List<String>> container = new ArrayList<>();
        column.add("参数名");
        column.add("参数解释");
        column.add("参数类型");
        column.add("限制规则");
        List<List<String>> data = new ArrayList<>();
        for (ClassFieldDesc desc : list) {
            log.debug("生成请求参数{}的解释",desc.getParameterName());
            List<String> row = new ArrayList<>();
            row.add(desc.getParameterName());
            row.add(Optional.ofNullable(MarkdownUtil.escapeString(desc.getParameterDesc())).orElse(""));
            row.add(desc.getDataType().getName());
            row.add("");
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
