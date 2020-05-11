package com.jiawei.jwBoot.service;


import com.jiawei.jwBoot.pojo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
public interface TestService {


    String helloGet();


    Object helloPost(HttpServletRequest request, HttpServletResponse response, String name, String sex, Integer age);

    Object helloPut(UserInfoVo userInfo);

    Object helloDelete(UserInfoVo userInfo);
}
