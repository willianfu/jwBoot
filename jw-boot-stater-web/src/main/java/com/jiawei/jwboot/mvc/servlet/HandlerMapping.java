package com.jiawei.jwboot.mvc.servlet;

import annotation.component.Component;
import bean.ioc.IocContainerContext;
import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.DeleteMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.GetMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PostMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PutMapping;
import com.jiawei.jwboot.annotation.component.controller.result.ResponseBody;
import org.slf4j.Logger;
import utils.AppUtil;
import utils.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Component
public class HandlerMapping {

    private static Logger log = AppUtil.getLogger(HandlerMapping.class);

    private static Map<String, HandlerMappingBean> handlerMapping = new ConcurrentHashMap<>();

    public static Map<String, HandlerMappingBean> getHandlerMapping() {
        return handlerMapping;
    }

    public void scanHandlerMapping(){
        IocContainerContext.getInstance()
                .getIocClassByAnnotation(Controller.class).forEach(this::handlerMappingInit);
        JwBootDispatchServlet.setDispatch(handlerMapping);
    }

    /**
     * 映射url 与对应controller方法
     *
     * @param clazz 类
     * @param instance
     */
    private void handlerMappingInit(Class<?> clazz, Object instance) {
        if (null != clazz.getAnnotation(Controller.class)) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Mapping requestMapping = getMethodRequestMapping(method);
                if (ObjectUtil.isNotNull(requestMapping)) {
                    String key = this.getRequestMappingUri(clazz, method, requestMapping);
                    HandlerMappingBean mappingBean = new HandlerMappingBean();
                    mappingBean.setInstance(instance);
                    mappingBean.setMethod(method);
                    mappingBean.setUri(key.trim());
                    if (ObjectUtil.isEmptyStr(mappingBean.getReturnType())){
                        //检查返回的是视图还是对象，默认返回视图
                        if (ObjectUtil.isNotNull(clazz.getAnnotation(ResponseBody.class) )
                                || ObjectUtil.isNotNull(method.getAnnotation(ResponseBody.class))){
                            mappingBean.setReturnType("Object");
                        }else {
                            mappingBean.setReturnType("View");
                        }
                    }
                    if (ObjectUtil.isNotNull(handlerMapping.get(mappingBean.getUri()))) {
                        throw new RuntimeException("请求路径 [" + mappingBean.getUri() + "] 已经定义过了");
                    }
                    log.info("Handler mapping controller:[{}] to method:[{}] URI:[{}]", clazz.getTypeName(), method.getName(), mappingBean.getUri());
                    handlerMapping.put(mappingBean.getUri(), mappingBean);
                }
            }
        }
    }


    /**
     * 获取完整URI路径映射
     * @param clazz 类
     * @param method 方法
     * @param requestMapping 映射
     * @return URI路径
     */
    private String getRequestMappingUri(Class<?> clazz, Method method, Mapping requestMapping){
        String uri = "";
        String classMethod = "";
        RequestMapping classMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
        String methodMapping = requestMapping.getMethod().getMethod();
        if (ObjectUtil.isNotNull(classMapping)){
            uri = classMapping.value().trim();
            classMethod = classMapping.method().getMethod();
        }
        //校验请求方法是否冲突
        if (!ObjectUtil.isEmptyStr(classMethod) && !ObjectUtil.isEmptyStr(methodMapping)){
            if (!classMethod.equalsIgnoreCase(methodMapping)){
                throw new RuntimeException(clazz.getTypeName() + " 中 " + method.getName() + " 与类上的注解URI映射，请求方法出现冲突，请检查");
            }
            //请求方法相同，舍弃方法标记使用类请求方法
            methodMapping = classMethod;
        }
        return methodMapping + " " + uri + requestMapping.getUri().trim();
    }

    private Mapping getMethodRequestMapping(Method method){
        for (Annotation annotation :method.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (Mapping.mappingMap.contains(annotationType)){
                String uri = "";
                HttpMethod httpMethod = null;
                if (annotationType.isAssignableFrom(RequestMapping.class)){
                    RequestMapping request = (RequestMapping) annotation;
                    uri = request.value();
                    httpMethod = request.method();
                }else {
                    RequestMapping requestMapping = annotationType.getAnnotation(RequestMapping.class);
                    if (null != requestMapping) {
                        httpMethod = requestMapping.method();
                        if (GetMapping.class.isAssignableFrom(annotationType)){
                            uri = ((GetMapping) annotation).value();
                        }else if (PutMapping.class.isAssignableFrom(annotationType)){
                            uri = ((PutMapping) annotation).value();
                        }else if (PostMapping.class.isAssignableFrom(annotationType)){
                            uri = ((PostMapping) annotation).value();
                        }else if (DeleteMapping.class.isAssignableFrom(annotationType)){
                            uri = ((DeleteMapping) annotation).value();
                        }
                    }
                }
                return new Mapping(uri, httpMethod);
            }
        }
        return null;
    }
}
