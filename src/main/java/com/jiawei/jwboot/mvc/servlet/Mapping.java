package com.jiawei.jwboot.mvc.servlet;

import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.DeleteMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.GetMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PostMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PutMapping;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class Mapping {

    private String uri;

    private HttpMethod method;

    public static Set<Class<? extends Annotation>> mappingMap = new HashSet<>();
    static {
        mappingMap.add(RequestMapping.class);
        mappingMap.add(GetMapping.class);
        mappingMap.add(PostMapping.class);
        mappingMap.add(PutMapping.class);
        mappingMap.add(DeleteMapping.class);
    }

    public Mapping(String uri, HttpMethod method) {
        this.uri = uri;
        this.method = method;
    }

    public Mapping() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }
}
