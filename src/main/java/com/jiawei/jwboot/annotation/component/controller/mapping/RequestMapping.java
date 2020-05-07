package com.jiawei.jwboot.annotation.component.controller.mapping;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * 处理器映射器，映射到具体的handler方法或者类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface RequestMapping {
    String value() default "/";
    HttpMethod method() default HttpMethod.ALL;
}
