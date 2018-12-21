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

}
