package com.jiawei.jwboot.mvc.ioc;


import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.mvc.servlet.HandlerMappingBean;
import com.jiawei.jwboot.utils.ObjectUtil;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : willian fu
 * @version : 1.0
 */

public class IocContainerContext extends AbstractIocContext implements IocContainer {

    private static final Map<String, HandlerMappingBean> HANDLER_MAPPING = new ConcurrentHashMap();

    /**
     * 从容器中获取一个Bean
     *
     * @param clazz bean类型
     * @return bean实例
     */
    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
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

    public Set<Class<?>> iocInit() {
        //扫描所有的包
        Reflections reflections = new Reflections();
        Set<Class<?>> component = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> classComponent = reflections.getTypesAnnotatedWith(Component.class);
        IocContainerContext instance = IocContainerContext.getInstance();
        // List<Class<?>> list = component.stream().filter(Class::isMemberClass).collect(Collectors.toList());
        for (Class<?> clazz : component) {
            //加载组件
            if (isClass(clazz)) {
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
        return classComponent;
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
                RequestMapping requestMapping = null;
                Annotation[] declaredAnnotations = method.getAnnotations();
                for (Annotation annotation :declaredAnnotations) {
                    if (method.getName().equals("controller2")){
                        System.out.println(RequestMapping.class.isAssignableFrom(annotation.annotationType()));
                        System.out.println(method.isAnnotationPresent(RequestMapping.class));
                        System.out.println(annotation.annotationType().getTypeName());
                        System.out.println();
                    }
                    requestMapping = (RequestMapping)annotation;
                    if (RequestMapping.class.isAssignableFrom(annotation.annotationType())){
                        requestMapping = (RequestMapping)annotation;
                    }
                }
                RequestMapping classMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
                if (null != requestMapping) {
                    String uri = "";
                    String classMethod = "";
                    String methodMapping = requestMapping.method().getMethod();
                    if (null != classMapping){
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
                    if (!ObjectUtil.isEmptyStr(uri)){
                        uri += requestMapping.value().trim();
                    }
                    String key = methodMapping + " " + uri;
                    HandlerMappingBean mappingBean = new HandlerMappingBean();
                    mappingBean.setInstance(instance);
                    mappingBean.setMethod(method);
                    mappingBean.setUri(key.trim());
                    if (null != HANDLER_MAPPING.get(mappingBean.getUri())) {
                        throw new RuntimeException("请求路径 [" + mappingBean.getUri() + "] 已经定义过了");
                    }
                    HANDLER_MAPPING.put(mappingBean.getUri(), mappingBean);
                }
            }
        }
    }

    public Map<String, HandlerMappingBean> getHandlerMapping() {
        return HANDLER_MAPPING;
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
