package com.jiawei.jwboot.annotation.component.controller.result;


import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ResponseBody {
}
