package com.jiawei.jwboot.annotation.component.controller.param;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * 此注解标记的注解将被
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestBody {
    boolean value() default true;
}
