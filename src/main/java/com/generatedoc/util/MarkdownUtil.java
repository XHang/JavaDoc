package com.generatedoc.util;

import com.generatedoc.exception.MarkDownException;

import java.util.List;

/**
 * Markdown 相关的工具类
 * @author  cxh
 * @createdate 2018-08-22
 */
public class MarkdownUtil {

    /**
     * 构建标题
     * @param level 标题星级
     * @param title 标题
     * @return
     */
    public static String buildTitle(int level,String title){
        if (level <=0){
            throw new MarkDownException("标题的星级必须大于1");
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<level;i++){
            sb.append("#");
        }
        sb.append(title);
        return  sb.toString();
    }

    /**
     * 构建代码块
     * @param code 代码块
     * @param codeType 代码类型
     * @return
     */
    public static String buildCodeArea(String code,String codeType){
        return null;
    }



    /**
     *  构建引用块
     * @param msg
     * @return
     */
    public static String buildReferenceBlock(String msg){
        return null;
    }


    /**
     *  构建有序列表
     * @param msg list里面可以嵌套，实现有序列表的嵌套
     * @return
     */
    public static String buildOrderedBlock(List<String> items){
        return null;
    }

    /**
     *  构建单个有序列表
     * @param itemId 序号
     * @param msg 列表的信息
     * @return
     */
    public static String buildSingleOrderItem(int itemId,String msg){
        return null;
    }




    /**
     *  构建无序列表
     * @param msg 无序列表 列表里面可以嵌套，实现无序列表的嵌套
     * @return
     */
    public static String buildDisOrderBlock(List<String> items){
        return null;
    }

    /**
     *  构建单个无序列表
     * @param msg 列表信息
     * @return
     */
    public static String buildSingleDisOrderItem(String msg){
        return null;
    }

    /**
     * 获取一个tab的空间
     * @return
     */
    public static String getTab(){
        return "    ";
    }

    /**
     * 构建有序列表的符号
     * @param itemId 序号
     * @return
     */
    public  static String buildOrderSymbol(int itemId){
        return itemId+". ";
    }


}
