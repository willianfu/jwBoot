package com.jiawei.jwboot.mvc.ioc;


import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.DeleteMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.GetMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PostMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PutMapping;
import com.jiawei.jwboot.annotation.component.controller.result.ResponseBody;
import com.jiawei.jwboot.mvc.di.ComponentsDependConfig;
import com.jiawei.jwboot.mvc.servlet.HandlerMappingBean;
import com.jiawei.jwboot.mvc.servlet.Mapping;
import com.jiawei.jwboot.utils.AppUtil;
import com.jiawei.jwboot.utils.ObjectUtil;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : willian fu
 * @version : 1.0
 */

public class IocContainerContext extends AbstractIocContext implements IocContainer {

    private static final Map<String, HandlerMappingBean> HANDLER_MAPPING = new ConcurrentHashMap();

    private static Logger log = AppUtil.getLogger(IocContainerContext.class);

    /**
     * 从容器中获取一个Bean
     *
     * @param clazz bean类型
     * @return bean实例
     */
    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        Object o = IOC_CONTAINER.get(clazz);
        return clazz.cast(IOC_CONTAINER.get(clazz));
    }

    /**
     * 将一个实例对象放入ioc容器中
     *
     * @param obj 对象
     */
    @Override
    public void putBeanToIoc(Object obj) {
        if (null == this.getBeanByClass(obj.getClass())) {
            IOC_CONTAINER.put(obj.getClass(), obj);
        } else {
            throw new RuntimeException("该实例已被IOC托管，无法覆盖");
        }
    }

    @Override
    public Set<Class<?>> iocInit() {
        //扫描所有的包
        Reflections reflections = null;
        try {
            reflections = new Reflections();
        } catch (Exception e) {

        }
        Set<Class<?>> component = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> classComponent = reflections.getTypesAnnotatedWith(Component.class);
        IocContainerContext instance = IocContainerContext.getInstance();
        // List<Class<?>> list = component.stream().filter(Class::isMemberClass).collect(Collectors.toList());
        for (Class<?> clazz : component) {
            //加载组件
            if (isClass(clazz)) {
                log.info("Scan to class [{}]", clazz.getTypeName());
                try {
                    Object instance1 = clazz.newInstance();
                    instance.putBeanToIoc(instance1);
                    handlerMappingInit(clazz, instance1);
                    classComponent.add(clazz);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("IOC初始化时，实例化类 [" + clazz.getTypeName() + "] 失败");
                }
            }
        }
        this.getBeanByClass(ComponentsDependConfig.class).init(IOC_CONTAINER);
        return classComponent;
    }

    /**
     * 获取某个类或接口的所有实现类
     * @param clazz 类型class
     * @return 类列表
     */
    @Override
    public <T> List<Class<T>> getSonClass(Class<T> clazz){
        List<Class<T>> classes = new ArrayList<>();
        IOC_CONTAINER.forEach((claz, component) ->{
            //取接口的所有子类
            if (clazz.isAssignableFrom(claz)){
                classes.add(claz);
            }
        });
        return classes;
    }
    /**
     * 映射url 与对应controller方法
     *
     * @param clazz    类
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
                    if (ObjectUtil.isNotNull(HANDLER_MAPPING.get(mappingBean.getUri()))) {
                        throw new RuntimeException("请求路径 [" + mappingBean.getUri() + "] 已经定义过了");
                    }
                    log.info("Handler mapping controller:[{}] to method:[{}] URI:[{}]", clazz.getTypeName(), method.getName(), mappingBean.getUri());
                    HANDLER_MAPPING.put(mappingBean.getUri(), mappingBean);
                }
            }
        }
    }

    public Map<String, HandlerMappingBean> getHandlerMapping() {
        return HANDLER_MAPPING;
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

    private static boolean isClass(Class<?> clazz) {
        return !(clazz.isInterface() || clazz.isAnnotation());
    }


    /**
     * 从容器中移除一个实例
     *
     * @param clazz bean类型
     * @return 被移除的bean
     */
    @Override
    public <T> T removeBeanByClass(Class<T> clazz) {
        return clazz.cast(IOC_CONTAINER.get(clazz));
    }

    private static boolean has = false;

    private IocContainerContext() {
        synchronized (IocContainerContext.class) {
            //防止通过反射实例化
            if (!has) {
                has = true;
            } else {
                throw new RuntimeException("强制单例，不允许再次实例化");
            }
        }
    }

    public synchronized static IocContainerContext getInstance() {
        return Builder.INSTANCE;
    }

    private static final class Builder {
        private static final IocContainerContext INSTANCE = new IocContainerContext();
    }
}
