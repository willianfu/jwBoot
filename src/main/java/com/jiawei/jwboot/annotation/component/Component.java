package com.jiawei.jwboot.annotation.component;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * JwBoot组件注解，被注解的类将被实例化托管进IOC
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Component {
}
