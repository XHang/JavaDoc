package com.generatedoc.constant;

import com.generatedoc.emnu.RequestType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringMVC 框架的一些常量
 */
public class SpringMVCConstant {

    /**
     * 控制器对外的方法的标识注解集合
     */
    public static final List<String> CONTROLLER_METHOD = new ArrayList<>();


    /**
     *  各个请求方式的注解名称
     */
    public static final String GET_ANNOTATION = "GetMapping";
    public static final String POST_ANNOTATION = "PostMapping";
    public static final String DELETE_ANNOTATION = "DeleteMapping";
    public static final String PATCH_ANNOTATION = "PatchMapping";
    public static final String PUT_ANNOTATION = "PutMapping";
    //不带任何请求方式的请求注解
    public static final String REQUEST_ANNOTATION = "RequestMapping";

    /**
     * requestMapping注解里面的请求方式属性名
     */
    public static final String REQUEST_METHOD = "method";

    public static final String REQUEST_URL_VALUE = "value";
    public static final String REQUEST_URL_PATH = "path";
    /**
     * 放置请求方式的注解和文档请求方式的对应关系
     */
    public static Map<String,RequestType> REQUEST_TYPE_MAP = new HashMap<>();

    /**
     * 返回值直接返回到前端的注解
     */
    public static final String RESPONSE_BODY = "ResponseBody";

    /**
     * 请求参数直接从请求体里面取的注解
     */
    public static final String REQUEST_BODY = "RequestBody";
    /**
     * 返回值直接返回到前端的注解
     */
    public static final String REST_CONTROLLER = "RestController";

    static {
        CONTROLLER_METHOD.add(GET_ANNOTATION);
        CONTROLLER_METHOD.add(POST_ANNOTATION);
        CONTROLLER_METHOD.add(DELETE_ANNOTATION);
        CONTROLLER_METHOD.add(PATCH_ANNOTATION);
        CONTROLLER_METHOD.add(REQUEST_ANNOTATION);
        CONTROLLER_METHOD.add(PUT_ANNOTATION);

        REQUEST_TYPE_MAP.put(GET_ANNOTATION,RequestType.GET);
        REQUEST_TYPE_MAP.put(POST_ANNOTATION,RequestType.POST);
        REQUEST_TYPE_MAP.put(DELETE_ANNOTATION,RequestType.DELETE);
        REQUEST_TYPE_MAP.put(PATCH_ANNOTATION,RequestType.PATCH);
        REQUEST_TYPE_MAP.put(PUT_ANNOTATION,RequestType.PUT);

    }

    /**
     * 不是请求参数的类型
     */
    public static final List<String> NOT_REQUEST_PARAMETER_TYPE = new ArrayList<>();
    static {
        NOT_REQUEST_PARAMETER_TYPE.add("BindingResult");
        NOT_REQUEST_PARAMETER_TYPE.add("HttpServletRequest");
        NOT_REQUEST_PARAMETER_TYPE.add("HttpServletResponse");
    }

    public static final List<String> CONTROLLER_NAMES = new ArrayList<>();
    static {
        CONTROLLER_NAMES.add("Controller");
        CONTROLLER_NAMES.add("RestController");
    }
    

}
