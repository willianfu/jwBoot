package com.jiawei.jwboot.annotation.component.configuration;

import com.jiawei.jwboot.annotation.component.Component;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * 配置类注解，被注解的类将会被代理，防止@Bean内部创建类被实例化2次
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
public @interface Configuration {

}
