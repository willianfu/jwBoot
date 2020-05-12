package com.jiawei.jwboot.annotation.component.controller;

import com.jiawei.jwboot.annotation.component.Component;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * controller 层注解，被托管的controller将被关联servlet
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
public @interface Controller {

}
