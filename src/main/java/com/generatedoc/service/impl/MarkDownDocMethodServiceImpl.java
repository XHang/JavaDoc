package com.generatedoc.service.impl;

import com.generatedoc.common.SymbolConstant;
import com.generatedoc.config.ApplicationConfig;
import com.generatedoc.emnu.RequestType;
import com.generatedoc.model.*;
import com.generatedoc.service.MarkDownDocMethodService;
import com.generatedoc.util.DateUitl;
import com.generatedoc.util.JSONUtil;
import com.generatedoc.util.MarkdownUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MarkDownDocMethodServiceImpl implements MarkDownDocMethodService {
     public static final Logger log = LoggerFactory.getLogger(MarkDownDocMethodServiceImpl.class);

     @Autowired
    ApplicationConfig config;

    @Override
    public Node buildMethodDoc(ApiInterface apiInterface) {
        log.debug("开始生成方法{}的接口文档",apiInterface.getDesc());
        Node interfaceNode = new Node();
        interfaceNode.setTitle(buildTitle(apiInterface));
        interfaceNode.getNodes().add(writeAuthor(apiInterface.getAuthor()));
        interfaceNode.getNodes().add(writeMethodDesc(apiInterface.getDesc()));
        interfaceNode.getNodes().add(writeRequestType(apiInterface.getRequestType()));
        interfaceNode.getNodes().add(writeRequestUrl(apiInterface.getUrl()));
        interfaceNode.getNodes().add(buildRequestArea(apiInterface));
        interfaceNode.getNodes().add(buildResponseArea(apiInterface));
        interfaceNode.getNodes().add(buildOtherDesc());
        return interfaceNode;
    }

    private Node buildOtherDesc() {
        Node otherDesc = new Node();
        otherDesc.setTitle("其他说明");
        otherDesc.setContent(MarkdownUtil.buildText(config.getNothing()));
        return otherDesc;
    }

    private Node writeCreateTime() {
        Node timeNode = new Node();
        StringBuilder sb = new StringBuilder();
        timeNode.setTitle("修订日期");
        timeNode.setContent(DateUitl.formatterChinaDate(LocalDateTime.now()));
        return timeNode;
    }

    private Node writeRequestUrl(String url) {
        Node urlNode = new Node();
        urlNode.setTitle("调用URL");
        urlNode.setContent(MarkdownUtil.buildText(url));
        return urlNode;
    }

    private Node  writeRequestType(RequestType requestType) {
        Node requestTypeNode = new Node();
        requestTypeNode.setTitle("调用方式");
        requestTypeNode.setContent(MarkdownUtil.buildText(requestType.name()));
        return requestTypeNode;
    }

    private Node writeMethodDesc(String desc) {
        Node methodDescNode = new Node();
        methodDescNode.setTitle("功能");
        methodDescNode.setContent(MarkdownUtil.buildText(desc));
        return methodDescNode;
    }

    private Node  writeAuthor(String author) {
        Node authorNode = new Node();
        StringBuilder sb = new StringBuilder();
        authorNode.setTitle("编写人员");
        authorNode.setContent(MarkdownUtil.buildText(author));
        return authorNode;
    }

    private Node buildResponseArea(ApiInterface apiInterface) {
        Node responseAreaNode = new Node();
        responseAreaNode.setTitle("响应解释");
        StringBuilder sb = new StringBuilder();
        buildResponseDesc(sb,apiInterface);
        buildResponseExample(sb,apiInterface);
        responseAreaNode.setContent(sb.toString());
        return responseAreaNode;
    }

    private void buildResponseExample(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildBoldText("响应示例"));
        sb.append(MarkdownUtil.buildCodeArea(JSONUtil.jsonFormat(apiInterface.getReturnExample()),"JSON"));
    }

    private void buildResponseDesc(StringBuilder sb, ApiInterface apiInterface) {
        ClassInfo classInfo = apiInterface.getResponseDesc();
        if (classInfo == null){
            sb.append(MarkdownUtil.buildText(config.getNothing()));
            return;
        }
        writeResponseBeanDesc(sb, classInfo);

    }
    private void writeResponseBeanDesc(StringBuilder write, ClassInfo beanDesc){
        if (CollectionUtils.isEmpty(beanDesc.getClassFieldInfos())) {
            write.append(MarkdownUtil.buildText(config.getNothing()));
            return;
        }
        List<ClassInfo> descList = writeResponseFieldDesc(write,beanDesc.getClassFieldInfos());
        if (!CollectionUtils.isEmpty(descList)){
            for (ClassInfo classInfo : descList) {
                if (CollectionUtils.isEmpty(classInfo.getClassFieldInfos())) {
                    continue;
                }
                write.append(MarkdownUtil.buildBoldText(classInfo.getClassDesc() +"字段解释"));
                writeResponseBeanDesc(write, classInfo);
            }
        }

    }
    private ArrayList<ClassInfo> writeResponseFieldDesc(StringBuilder write, List<ClassFieldInfo> fields){
        List<String> column = new ArrayList<>();
        column.add("字段名");
        column.add("字段解释");
        column.add("字段类型");
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        MarkDownTableDto dto = new MarkDownTableDto();
        dto.setColumns(column);
        List<List<String>> data = new ArrayList<>();
        for (ClassFieldInfo field : fields) {
            log.debug("生成响应参数{}的解释",field.getParameterName());
            List<String> row = new ArrayList<>();
            row.add(field.getParameterName());
            row.add(Optional.ofNullable(field.getParameterDesc()).orElse(config.getNothing()));
            row.add(field.getDataType().getName());
            if (field.getNestingClssDesc() != null) {
                classInfos.add(field.getNestingClssDesc());
            }
            data.add(row);
        }
        dto.setData(data);
        write.append(MarkdownUtil.buildTable(dto));
        return classInfos;
    }

    private void buildRequestExample(StringBuilder sb, ApiInterface apiInterface) {
        sb.append(MarkdownUtil.buildBoldText("调用示例"));
        sb.append((MarkdownUtil.buildCodeArea(JSONUtil.jsonFormat(apiInterface.getParameterExample()),"JSON")));
    }

    private Node buildRequestArea( ApiInterface apiInterface) {
        Node requestAreaNode = new Node();
        requestAreaNode.setTitle("参数说明");
        StringBuilder sb = new StringBuilder();
        buildHeadParameterDesc(sb, apiInterface.getHeaderParameters());
        buildBodyParameterDesc(sb, apiInterface.getBodyParameter());
        buildRequestExample(sb,apiInterface);
        requestAreaNode.setContent(sb.toString());
        return requestAreaNode;
    }

    private void buildBodyParameterDesc(StringBuilder sb, ClassInfo classInfo) {
        sb.append(MarkdownUtil.buildBoldText("请求体参数"));
        if (classInfo == null){
            sb.append(MarkdownUtil.buildText(config.getNothing()));
            return;
        }
        writerClassDesc(sb, classInfo);
    }
    private void writerClassDesc(StringBuilder write, ClassInfo desc){
            if (CollectionUtils.isEmpty(desc.getClassFieldInfos())){
                return;
            }
            write.append(MarkdownUtil.buildBoldText(String.format("%s%s字段解释",desc.getClassDesc(),desc.getDataType().getName())));
            List<ClassInfo> classInfoList =  writeFieldDesc(desc.getClassFieldInfos(),write);
            if (!CollectionUtils.isEmpty(classInfoList)){
                for (ClassInfo classInfo : classInfoList) {
                    if (CollectionUtils.isEmpty(classInfo.getClassFieldInfos())){
                        continue;
                    }
                    //write.append(MarkdownUtil.buildBoldText(classInfo.getClassDesc()+"字段解释"));
                    writerClassDesc(write, classInfo);
                }
            }
    }

    private void buildHeadParameterDesc(StringBuilder sb, List<HeadParameterInfo> list){
        sb.append(MarkdownUtil.buildBoldText("请求头参数"));
        if (CollectionUtils.isEmpty(list)){
            sb.append(MarkdownUtil.buildText(config.getNothing()));
            return;
        }
         writeFieldDesc(list,sb);
    }

    /**
     * 将类字段写到接口文档里面
     * @param list 类字段
     * @param write 接口文档写入器
     * @return List<ClassInfo> 嵌套Bean描述
     */
    private List<ClassInfo> writeFieldDesc(List<? extends ClassFieldInfo> list, StringBuilder write) {
        MarkDownTableDto dto = new MarkDownTableDto();
        List<String> column =  new ArrayList<>();
        List<ClassInfo> nestingBean = new ArrayList<>();
        column.add("参数名");
        column.add("参数解释");
        column.add("参数类型");
        column.add("限制规则");
        List<List<String>> data = new ArrayList<>();
        for (ClassFieldInfo desc : list) {
            log.debug("生成请求参数{}的解释",desc.getParameterName());
            List<String> row = new ArrayList<>();
            row.add(desc.getParameterName());
            row.add(Optional.ofNullable(MarkdownUtil.escapeString(desc.getParameterDesc())).orElse(config.getNothing()));
            row.add(desc.getDataType().getName());
            row.add(getRuleInfo(desc));
            data.add(row);
            if (desc.getNestingClssDesc() != null) {
                nestingBean.add(desc.getNestingClssDesc());
            }
        }
        dto.setColumns(column);
        dto.setData(data);
        write.append(MarkdownUtil.buildTable(dto));
        return nestingBean;
    }

    private String getRuleInfo(ClassFieldInfo desc) {
        List<FieldRule> rules = desc.getFieldRule();
        if (CollectionUtils.isEmpty(rules)){
            return config.getNothing();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            FieldRule rule = rules.get(i);
            sb.append(MarkdownUtil.buildSingleOrderItemNoWarp(i,rule.getRuleType().getName()+"  限制值："+MarkdownUtil.buildCodeLineNoWrap(rule.getRuleValue())));
            sb.append(SymbolConstant.BR_SYMBOL);
        }
        return sb.toString();
    }


    private String  buildTitle(ApiInterface apiInterface) {
        return apiInterface.getDesc();
    }
}
