package com.jiawei.jwboot.mvc.servlet;

import com.jiawei.jwboot.annotation.component.controller.param.RequestBody;
import com.jiawei.jwboot.annotation.component.controller.param.RequestParam;
import com.jiawei.jwboot.utils.HttpUtil;
import org.slf4j.Logger;
import utils.AppUtil;
import utils.ObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class JwBootDispatchServlet extends HttpServlet {

    private static Logger log = AppUtil.getLogger(JwBootDispatchServlet.class);

    private static Map<String, HandlerMappingBean> dispatch;

    public static void setDispatch(Map<String, HandlerMappingBean> mapping) {
        dispatch = mapping;
    }

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
        HandlerMappingBean handlerMapping = dispatch.get(req.getMethod() + " " + req.getRequestURI());
        if (null == handlerMapping){
            HttpUtil.returnNotFound(resp);
        }else {
            this.doHandler(req, resp, handlerMapping);
        }
    }

    private void doHandler(HttpServletRequest req,  HttpServletResponse resp, HandlerMappingBean handlerMapping){
        //boolean isJson = req.getContentType().contains("application/json");
        log.info("Received request [{} {}]", req.getMethod(), req.getRequestURI());
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                            HttpUtil.returnParamBad(resp, "Parameter " + requestParam.value() + " is necessary");
                            return;
                        }else {
                            //设置默认值
                            try {
                                params[i] = ObjectUtil.typeCover(parameters[i].getType(), requestParam.defaultValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                                HttpUtil.returnParamBad(resp, "Default value parameter " + requestParam.value() + " Cannot be converted into " + parameters[i].getType().getTypeName());
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
            handlerMapping.getReturn(resp, returns);
        } catch (IOException e) {
            e.printStackTrace();
            HttpUtil.returnServerError(resp, "Cannot found html view");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            HttpUtil.returnParamBad(resp,"Parameters cannot be mapped correctly \n URL: " + handlerMapping.getUri());
        }
    }
}
