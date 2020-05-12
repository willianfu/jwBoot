package com.jiawei.jwboot.annotation.component.controller.mapping;

/**
 * @author : willian fu
 * @version : 1.0
 * Http请求
 */
public enum HttpMethod {
    //表示所有方法
    ALL(""),
    //get方法
    GET("GET"),

    POST("POST"),

    PUT("PUT"),

    DELETE("DELETE"),

    OPTION("OPTION");

    private String method;

    public String getMethod() {
        return method;
    }

    HttpMethod(String method) {
        this.method = method;
    }
}
