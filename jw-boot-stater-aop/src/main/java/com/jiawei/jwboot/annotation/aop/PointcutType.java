package com.jiawei.jwboot.annotation.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author : willian fu
 * @version : 1.0
 * 切入点类型，暂时只支持方法切入和注解接入
 */
public enum PointcutType {

    //方法切入
    METHOD(Method.class),

    //注解切入
    ANNOTATION(Annotation.class);

    private Class<?> point;

    public Class<?> getPoint() {
        return point;
    }

    PointcutType(Class<?> point) {
        this.point = point;
    }
}
