package com.jiawei.jwboot.mvc.servlet;

import com.jiawei.jwboot.annotation.component.controller.param.RequestBody;
import com.jiawei.jwboot.annotation.component.controller.param.RequestParam;
import com.jiawei.jwboot.annotation.component.controller.result.ResponseBody;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;
import com.jiawei.jwboot.utils.HttpUtil;
import com.jiawei.jwboot.utils.ObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class JwBootDispatchServlet extends HttpServlet {

    public static Map<String, HandlerMappingBean> dispatch = IocContainerContext.getInstance().getHandlerMapping();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        this.handler(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp){
        this.handler(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        this.handler(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        this.handler(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp){
        this.handler(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp){
        this.handler(req, resp);
    }

    private void handler(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("====>>>>收到请求 " + req.getRequestURI());
        HandlerMappingBean handlerMapping = dispatch.get(req.getMethod() + " " + req.getRequestURI());
        if (null == handlerMapping){
            HttpUtil.returnNotFound(resp);
        }else {
            this.doHandler(req, resp, handlerMapping);
        }
    }

    private void doHandler(HttpServletRequest req,  HttpServletResponse resp, HandlerMappingBean handlerMapping){
        //boolean isJson = req.getContentType().contains("application/json");
        boolean isJson = true;
        //获取处理方法
        Method mappingMethod = handlerMapping.getMethod();
        //获取所有参数
        Parameter[] parameters = mappingMethod.getParameters();
        //构造全部参数进行注入
        Object[] params = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (HttpServletRequest.class.isAssignableFrom(parameters[i].getType())){
                params[i] = req;
            }else if (HttpServletResponse.class.isAssignableFrom(parameters[i].getType())){
                params[i] = resp;
            }else {
                //其他类型的参数注入，必须带注解
                RequestParam requestParam = parameters[i].getDeclaredAnnotation(RequestParam.class);
                RequestBody responseBody = parameters[i].getDeclaredAnnotation(RequestBody.class);
                if (null != requestParam){
                    //取参数名
                    String parameter = req.getParameter(requestParam.value());
                    if (ObjectUtil.isEmptyStr(parameter)){
                        if (requestParam.required()){
                            HttpUtil.returnParamBad(resp, "Parameter " + parameters[i].getName() + " is necessary");
                            return;
                        }else {
                            //设置默认值
                            try {
                                params[i] = ObjectUtil.typeCover(parameters[i].getType(), requestParam.defaultValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                                HttpUtil.returnParamBad(resp, "Default value parameter " + parameters[i].getName() + " Cannot be converted into " + parameters[i].getType().getTypeName());
                                return;
                            }
                        }
                    }else {
                        params[i] = ObjectUtil.typeCover(parameters[i].getType(), parameter);
                    }
                }else if (null != responseBody && isJson){
                    params[i] = HttpUtil.getRequestBody(req, parameters[i].getType());
                }
            }
        }
        //执行参数注入
        try {
            Object returns = handlerMapping.getMethod().invoke(handlerMapping.getInstance(), params);
            //构造返回数据
            String returnType = handlerMapping.getReturnType();
            if ("View".equals(returnType)){
                HttpUtil.writerToResponseHtml(resp, getViewByName(returns.toString()));
            }else if ("Object".equals(returnType)){
                HttpUtil.writerToResponseJson(resp, returns);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            HttpUtil.returnParamBad(resp,"Parameters cannot be mapped correctly \n URL: " + handlerMapping.getUri());
        }
    }

    private String getViewByName(String html){
        return "应当返回html视图，视图解析部分还未完成";
    }

}
