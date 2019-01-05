package com.generatedoc.model;

import java.util.List;

/**
 * MarkDown表格的实体类
 */
public class MarkDownTableDto {

    //标题
    private List<String> columns ;

    //表格的数据，外层的list是row，里层的list是column
    private List<List<String>> data;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
