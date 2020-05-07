package com.jiawei.jwboot.mvc.servlet;

import java.lang.reflect.Method;

/**
 * @author : willian fu
 * @version : 1.0
 */

public class HandlerMappingBean {

    //映射路径
    private String uri;

    //controller处理方法
    private Method method;

    //controller实例
    private Object instance;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
