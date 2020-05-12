package com.jiawei.jwboot.annotation.component.controller.mapping.subinterface;

import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@RequestMapping(method = HttpMethod.GET)
public @interface GetMapping {
    String value() default "";
}
