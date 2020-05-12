package com.jiawei.jwBoot.aspect;

import com.jiawei.jwboot.annotation.component.aop.Aspect;
import com.jiawei.jwboot.annotation.component.aop.Pointcut;
import com.jiawei.jwboot.annotation.component.aop.PointcutType;
import com.jiawei.jwboot.annotation.component.aop.notice.Around;
import com.jiawei.jwboot.annotation.component.aop.notice.Before;

import java.lang.reflect.Method;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Aspect
public class AopTest {

    @Pointcut(value = PointcutType.METHOD, rule = "**getHello**")
    public void pointcut1(){

    }

    @Pointcut(value = PointcutType.ANNOTATION,
            rule = "(com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.GetMapping)")
    public void pointcut2(){
        
    }

    @Around()
    public void before(){

    }
}
