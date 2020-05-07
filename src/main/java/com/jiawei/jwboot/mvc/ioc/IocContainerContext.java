package com.jiawei.jwboot.mvc.ioc;


import com.jiawei.jwboot.annotation.component.Component;
import org.reflections.Reflections;

import java.util.Set;

/**
 * @author : willian fu
 * @version : 1.0
 */

public class IocContainerContext extends AbstractIocContext implements IocContainer{

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
        if (null == this.getBeanByClass(obj.getClass())){
            IOC_CONTAINER.put(obj.getClass(), obj);
        }else {
            throw new RuntimeException("该实例已被IOC托管，无法覆盖");
        }
    }

    public Set<Class<?>> iocInit() {
        //扫描所有的包
        Reflections reflections = new Reflections();
        Set<Class<?>> component = reflections.getTypesAnnotatedWith(Component.class);
        IocContainerContext instance = IocContainerContext.getInstance();
        // List<Class<?>> list = component.stream().filter(Class::isMemberClass).collect(Collectors.toList());
        for (Class<?> clazz : component) {
            //加载组件
            if (isClass(clazz)) {
                try {
                    instance.putBeanToIoc(clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("IOC初始化时，实例化类 [" + clazz.getTypeName() + "] 失败");
                }
            }else {
                component.remove(clazz);
            }
        }
        return component;
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

    private IocContainerContext(){
        synchronized (IocContainerContext.class){
            //防止通过反射实例化
            if(!has){
                has = true;
            }else{
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
