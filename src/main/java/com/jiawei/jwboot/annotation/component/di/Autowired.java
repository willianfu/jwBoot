package com.jiawei.jwboot.annotation.component.di;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Autowired {
    //如果有多个实现类，value值不能为空，必须选择应该注入哪个实现类
    Class<?> value() default Autowired.class;
}
