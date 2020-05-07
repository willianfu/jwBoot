package com.jiawei.jwboot.annotation.component.controller;

import java.lang.annotation.*;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Controller
public @interface RestController {
}
