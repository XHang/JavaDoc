package com.generatedoc.service.impl;

import com.generatedoc.constant.NumberConstant;
import com.generatedoc.model.APIDocument;
import com.generatedoc.model.ApiInterface;
import com.generatedoc.model.Node;
import com.generatedoc.service.DocmentsService;
import com.generatedoc.service.MarkDownDocMethodService;
import com.generatedoc.util.DateUitl;
import com.generatedoc.util.IOUtil;
import com.generatedoc.util.MarkdownUtil;
import com.generatedoc.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class MarkdownDocServiceImpl implements DocmentsService {
    public static final Logger log = LoggerFactory.getLogger(DocmentsService.class);
    public static final String suffixName  ="md";
    final int topLevel = 1;

    @Autowired
    private MarkDownDocMethodService methodService;


    @Override
    public void saveDoc(APIDocument apiDocument, String descPath) {
        String path = buildFilePath(descPath,apiDocument);
        log.debug("开始生成接口文档{}",apiDocument.getDocumentName());
        Node documentNode = buildContent(apiDocument);
        String content = writeToString(documentNode);
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
    private Node buildContent(APIDocument apiDocument) {
        Node document = new Node();
        buildTitle(apiDocument,document);
        document.getNodes().add(buildDesc(apiDocument));
        document.getNodes().add(buildInterfactMethod(apiDocument));
        document.getNodes().add(writeCreateTime());
        return document;
    }
    private Node writeCreateTime() {
        Node timeNode = new Node();
        StringBuilder sb = new StringBuilder();
        timeNode.setTitle("修订日期");
        timeNode.setContent(DateUitl.formatterChinaDate(LocalDateTime.now()));
        return timeNode;
    }

    private Node buildInterfactMethod(APIDocument apiDocument) {
        Node interfaceList = new Node();
        interfaceList.setTitle("接口列表");
        List<ApiInterface> interfaces = apiDocument.getApiInterface();
        if (CollectionUtils.isNotEmpty(interfaces)){
            for (ApiInterface api : interfaces) {
                Node interfaceNode = methodService.buildMethodDoc(api);
                interfaceList.getNodes().add(interfaceNode);
            }
        }
        return interfaceList;
    }

    private void buildMethodDoc(ApiInterface apiInterface, StringBuilder sb,int index) {
        sb.append(MarkdownUtil.buildTitle(2, NumberConstant.NUMBER_MAP.get(index)+""));
    }

    private Node buildDesc(APIDocument apiDocument) {
        Node descNode = new Node();
        descNode.setTitle("描述");
        descNode.setContent(MarkdownUtil.buildText(apiDocument.getInterfaceDesc()));
        return descNode;
    }

    private void buildTitle(APIDocument apiDocument, Node node) {
        node.setTitle(apiDocument.getDocumentName()+"接口文档");
    }

    public String writeToString(Node document){
        StringBuilder sb  = new StringBuilder();
        sb.append(MarkdownUtil.buildTitle(topLevel,document.getTitle()));
        if (CollectionUtils.isNotEmpty(document.getNodes())){
            for (int i = 1; i <= document.getNodes().size(); i++) {
                Node node = document.getNodes().get(i-1);
                sb.append( writeNode(node,topLevel,i,""));
            }
        }
        return sb.toString();
    }
    private StringBuilder writeNode(Node node,int level,int number,String titlePreFix){
        StringBuilder sb = new StringBuilder();
        //当标题是第一层时，标题前面的数字要大写
        String prefix = buildPrefix(level==1,number,titlePreFix);
        sb.append(MarkdownUtil.buildTitle(level,prefix+": "+node.getTitle()));
        sb.append(MarkdownUtil.buildText(node.getContent()));
        if (level == 1){
            prefix =buildPrefix(false,number,titlePreFix);
        }
        if (CollectionUtils.isNotEmpty(node.getNodes())) {
            for (int i = 1; i <= node.getNodes().size(); i++) {
                Node childNode = node.getNodes().get(i - 1);
                sb.append(writeNode(childNode, level + 1, i,prefix));
            }
        }
        return sb;
    }
    private String buildPrefix(boolean isUppercase,int number,String preFix){
        //如果是最外的章节，则章节前缀是大写数字，否则是子章节，章节前缀是如1.2 之类的
        if (isUppercase){
            return NumberConstant.numberConverter(number)+" ";
        }else if (StringUtil.isEmpty(preFix)){
            return number+"";
        }else{
            return preFix+"."+number;
        }
    }
}
