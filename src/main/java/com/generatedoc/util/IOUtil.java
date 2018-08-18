package com.generatedoc.util;

import com.generatedoc.exception.DOCError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
}
