package com.generatedoc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generatedoc.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {
     public static final ObjectMapper mapper = new ObjectMapper();
      public static final Logger log = LoggerFactory.getLogger(JSONUtil.class);

     public static String toJSONString(Object object){
          try {
               return mapper.writeValueAsString(object);
          } catch (JsonProcessingException e) {
               throw new SystemException(e,"序列化异常");
          }
     }

     /**
      * 对json进行格式化输出
      * @param json
      * @return]
      *
      */
     public static  String jsonFormat(String json){
          //json格式化其实就是json打碎重组,所以搞一个重组的容器
          if (StringUtil.isEmpty(json)){
               return "";
          }
          StringBuilder sb = new StringBuilder();
          int indentCount = 0;
          for (int i=0;i<json.length();i++){
               char c = json.charAt(i);
               switch(c){
                    case '{':
                    case '[': {
                         sb.append(c);
                         sb.append(System.lineSeparator());
                         indentCount++;
                         //向下方补缩进
                         sb.append(getIndent(indentCount));
                         break;
                    }
                    case '}':
                    case ']':{
                         indentCount--;
                         sb.append(System.lineSeparator());
                         sb.append(getIndent(indentCount)+c);
                         break;
                    }
                    //如果都
                    case ',':{
                         //什么，害怕角标越界，然而实际上，json最后一个字符绝不可能是逗号
                         char nextChar = json.charAt(i+1);
                         sb.append(c);
                         //如果逗号后面紧跟着双引号，即 ,”,则逗号后面才需要换行
                         if (new String(new char[]{c,nextChar}).equals(",\"")){
                              sb.append(System.lineSeparator());
                              sb.append(getIndent(indentCount));
                         }
                         break;
                    }
                    default:{
                         sb.append(c);
                    }
               }
          }
          return sb.toString();
     }

     public static  String getIndent(int indentCount){
          StringBuilder sb = new StringBuilder();
          for (int i = 0;i < indentCount ;i++){
               sb.append('\t');
          }
          return sb.toString();
     }

}
