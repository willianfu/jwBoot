package com.jiawei.jwboot.annotation.component.controller.result;

import com.jiawei.jwboot.annotation.component.controller.Controller;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Controller
@ResponseBody
public @interface RestController {
}
