package com.jiawei.jwboot.test;

import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PostMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PutMapping;
import com.jiawei.jwboot.annotation.component.controller.param.RequestBody;
import com.jiawei.jwboot.annotation.component.controller.param.RequestParam;
import com.jiawei.jwboot.annotation.component.controller.result.ResponseBody;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Controller
public class TestController {

    @RequestMapping(value = "/test", method = HttpMethod.GET)
    public Object controller1(@RequestParam("name") String name,
                              @RequestParam("age") Integer age){
        System.out.println("收到GET请求数据 => name=" + name + " age="+age);
        return "收到GET请求数据 => name=" + name + " age="+age;
    }

    @ResponseBody
    @PutMapping(value = "/test")
    public Object controller(@RequestBody BeanB beanB){
        System.out.println("收到PUT请求数据 => " + beanB.toString());
        return beanB;
    }

    @ResponseBody
    @PostMapping(value = "/test")
    public Object controller2(@RequestBody BeanB beanB){
        System.out.println("收到POST请求数据 => " + beanB.toString());
        return beanB;
    }
}
