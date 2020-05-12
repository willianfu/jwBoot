package com.jiawei.jwBoot.service.impl;

import com.jiawei.jwBoot.pojo.UserInfoVo;
import com.jiawei.jwBoot.service.TestService;
import com.jiawei.jwboot.annotation.component.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String helloGet() {
        return "hello";
    }

    @Override
    public Object helloPost(HttpServletRequest request, HttpServletResponse response, String name, String sex, Integer age) {
        System.out.println("收到 post 请求 URI=" + request.getRequestURI());
        System.out.println("收到参数 name=" + name + " sex=" + sex + " age=" + age);
        return new UserInfoVo(name, sex, age);
    }

    @Override
    public Object helloPut(UserInfoVo userInfo) {
        System.out.println("收到 Put 请求参数=" + userInfo.toString());
        return userInfo;
    }

    @Override
    public Object helloDelete(UserInfoVo userInfo) {
        System.out.println("收到 Delete 请求参数=" + userInfo.toString());
        return userInfo;
    }


}
