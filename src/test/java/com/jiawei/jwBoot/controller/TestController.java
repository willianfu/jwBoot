package com.jiawei.jwBoot.controller;

import com.jiawei.jwBoot.pojo.UserInfoVo;
import com.jiawei.jwBoot.service.TestService;
import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.DeleteMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.GetMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PostMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PutMapping;
import com.jiawei.jwboot.annotation.component.controller.param.RequestBody;
import com.jiawei.jwboot.annotation.component.controller.param.RequestParam;
import com.jiawei.jwboot.annotation.component.controller.result.ResponseBody;
import com.jiawei.jwboot.annotation.component.di.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
@Controller
@RequestMapping("/jwboot")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public String helloGet(){
        return testService.helloGet();
    }

    @ResponseBody
    @PostMapping("/hello")
    public Object helloPost(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam("name") String name,
                            @RequestParam(value = "sex", required = false) String sex,
                            @RequestParam(value = "age", defaultValue = "20") Integer age){
        return testService.helloPost(request, response, name, sex, age);
    }

    @ResponseBody
    @PutMapping("/hello")
    public Object helloPut(@RequestBody UserInfoVo userInfo){
        return testService.helloPut(userInfo);
    }

    @ResponseBody
    @DeleteMapping("/hello")
    public Object helloDelete(@RequestBody UserInfoVo userInfo){
        return testService.helloDelete(userInfo);
    }
}
