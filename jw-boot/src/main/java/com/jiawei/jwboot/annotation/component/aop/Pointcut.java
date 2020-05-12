package com.jiawei.jwboot.annotation.component.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Pointcut {

    //切入点对象的class对象 可以是注解 方法
    PointcutType value();

    //切入点的匹配规则
    String rule();

    //目标类，可以指定切入哪些类
    Class<?>[] target() default {};
}
