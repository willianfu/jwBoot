package com.jiawei.jwboot.annotation.component.controller.param;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * controller 参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestParam {

    //参数名
    String value() default "";

    //参数是否必须
    boolean required() default true;

    //参数默认值
    String defaultValue() default "null";

}
