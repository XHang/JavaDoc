package com.generatedoc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * markDown的每一节
 */
public class Node {
    /**
     * 该小节的子节列表
     */
    private List<Node> nodes = new ArrayList<>();
    /**
     * 节的内容
     */
    private String content = "   ";

    /**
     * 节的标题
     */
    private String title = "   ";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
