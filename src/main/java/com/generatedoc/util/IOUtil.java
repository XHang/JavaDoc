package com.generatedoc.util;

import com.generatedoc.exception.DOCError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 *
 */
public class IOUtil {


    /**
     * 从标准输入里面获取数据
     * @return 数据
     */
    public static  String getInput(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            throw new DOCError("处理键盘输入失败");
        }
    }

    /**
     * 保存文件到磁盘中
     * @param fileContent  文件内容
     * @param descPath 目标路径
     */
    public static void saveFile(String fileContent,String descPath){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descPath),"utf-8"));
            writer.write(fileContent);
        } catch (IOException e) {
            throw new DOCError("保存文件失败",e);
        }finally{
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                throw new DOCError("关闭文件流失败",e);
            }
        }

    }
}
