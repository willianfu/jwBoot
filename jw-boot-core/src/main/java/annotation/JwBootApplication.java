package annotation;


import annotation.component.Component;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 * 应用启动入口，被注解的类将是启动类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
public @interface JwBootApplication {

}
