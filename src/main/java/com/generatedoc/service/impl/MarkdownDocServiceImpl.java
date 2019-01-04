package com.generatedoc.service.impl;

import com.generatedoc.constant.MappingConstant;
import com.generatedoc.entity.APIDocument;
import com.generatedoc.entity.ApiInterface;
import com.generatedoc.service.DocmentsService;
import com.generatedoc.service.MarkDownDocMethodService;
import com.generatedoc.util.IOUtil;
import com.generatedoc.util.MarkdownUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MarkdownDocServiceImpl implements DocmentsService {
    public static final Logger log = LoggerFactory.getLogger(DocmentsService.class);
    public static final String suffixName  ="md";
    @Autowired
    private MarkDownDocMethodService methodService;


    @Override
    public void saveDoc(APIDocument apiDocument, String descPath) {
        String path = buildFilePath(descPath,apiDocument);
        log.debug("开始生成接口文档{}",apiDocument.getDocumentName());
        String content = buildContent(apiDocument);
        IOUtil.saveFile(content,path);
    }

    private String buildFilePath(String descPath, APIDocument apiDocument) {
        log.debug("接口文档的保存目录是【{}】",descPath);
        //后缀名可自定义
        String path = descPath+"/"+apiDocument.getDocumentName()+"接口文档."+suffixName;
        return path;
    }

    /**
     * @param apiDocument
     * @return
     */
    private String buildContent(APIDocument apiDocument) {
        StringBuilder sb = new StringBuilder();
        buildTitle(apiDocument,sb);
        buildDesc(apiDocument,sb);
        buildInterfactMethod(apiDocument,sb);
        return sb.toString();
    }

    private void buildInterfactMethod(APIDocument apiDocument, StringBuilder sb) {
        List<ApiInterface> interfaces = apiDocument.getApiInterface();
        if (CollectionUtils.isEmpty(interfaces)){
            return;
        }
        for (int index = 0; index < interfaces.size(); index++) {
            ApiInterface apiInterface = interfaces.get(index);
            //+2是标题一被占用了
            methodService.buildMethodDoc(apiInterface,sb,index+2);
        }
    }

    private void buildMethodDoc(ApiInterface apiInterface, StringBuilder sb,int index) {
        sb.append(MarkdownUtil.buildTitle(2,MappingConstant.NUMBER_MAP.get(index)+""));
    }

    private void buildDesc(APIDocument apiDocument, StringBuilder sb) {
        String desc = MarkdownUtil.buildTitle(2,"一：描述");
        String descContent = MarkdownUtil.buildText(apiDocument.getInterfaceDesc());
        sb.append(desc);
        sb.append(descContent);
    }

    private void buildTitle(APIDocument apiDocument, StringBuilder sb) {
        String title = MarkdownUtil.buildTitle(1,apiDocument.getDocumentName()+"接口文档");
        sb.append(title);
    }
}
