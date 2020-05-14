package com.jiawei.jwboot.mvc.servlet;

import com.alibaba.fastjson.JSON;
import com.jiawei.jwboot.utils.HttpUtil;
import utils.ObjectUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * @author : willian fu
 * @version : 1.0
 */

public class HandlerMappingBean {

    //映射路径
    private String uri;

    //controller处理方法
    private Method method;

    //controller实例
    private Object instance;

    private String returnType;

    /**
     * 根据类型返回相应的值
     * @param response 响应
     * @param object controller方法返回值
     */
    public void getReturn(HttpServletResponse response, Object object) throws IOException {
        if ("View".equals(this.returnType)){
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            //寻找视图
            //InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("template/hello.html");
            InputStream viewInputStream = this.getClass().getClassLoader().getResourceAsStream("template/" + object.toString() + ".html");
            if (!ObjectUtil.isNotNull(viewInputStream)){
                HttpUtil.returnNotFound(response);
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(viewInputStream));
            String line = null;
            while (ObjectUtil.isNotNull(line = reader.readLine())){
                response.getWriter().append(line);
            }
            viewInputStream.close();
        }else if("Object".equals(this.returnType)){
            //对象转json
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JSON.toJSONString(object));
        }else {

        }
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
