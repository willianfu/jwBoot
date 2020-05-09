package com.jiawei.jwboot.mvc.di;

import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.annotation.component.di.Autowired;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Component
public class ComponentsDependConfig {

    public void init(Map<Class, Object> ioc){
        ioc.forEach((clazz, component) ->{
            //遍历所有实现类
            for (Field field : clazz.getDeclaredFields()) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (null != autowired){
                    Class<?> value = autowired.value();
                    field.setAccessible(true);
                    try {
                        field.set(component, getInstance(field.getType(), ioc, autowired));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("无法向 ["+ clazz.getTypeName()+"] 注入 [" + field.getName() +"] 依赖");
                    } catch (RuntimeException e){
                        throw new RuntimeException("无法向 ["+ clazz.getTypeName()+"] 注入 [" + field.getName() +"] 依赖 因为 "+ e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 获取某接口类/的所有子类
     * @param clazz
     * @param ioc
     * @return
     */
    private List<Class<?>> getSonClass(Class<?> clazz, Map<Class, Object> ioc){
        List<Class<?>> classes = new ArrayList<>();
        ioc.forEach((claz, component) ->{
            //取接口的所有子类
            if (clazz.isAssignableFrom(claz)){
                classes.add(claz);
            }
        });
        return classes;
    }

    private Object getInstance(Class<?> clazz, Map<Class, Object> ioc, Autowired autowired){
        //判断注解是否写了值
        if (!autowired.value().isAssignableFrom(Autowired.class)){
            return IocContainerContext.getInstance().getBeanByClass(autowired.value());
        }else {
            List<Class<?>> sonClass = getSonClass(clazz, ioc);
            if (sonClass.size() > 1){
                //扫描到多个实现类
                throw new RuntimeException("依赖属性类型 [" + clazz.getTypeName() + "] 注入时发现有属性多个实现类，请指明具体注入哪个");
            }else if (sonClass.size() == 1){
                return IocContainerContext.getInstance().getBeanByClass(sonClass.get(0));
            }
            return null;
        }
    }
}
