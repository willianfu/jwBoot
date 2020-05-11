package com.jiawei.jwboot.mvc.di;

import com.jiawei.jwboot.JwApplication;
import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.annotation.component.di.Autowired;
import com.jiawei.jwboot.annotation.component.di.Value;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Component
public class ComponentsDependConfig {

    public void init(Map<Class, Object> ioc){
        //先加载配置
        Properties properties = this.propertiesLoad("application.properties");
        ioc.put(Properties.class, properties);
        ioc.forEach((clazz, component) ->{
            //遍历所有实现类
            for (Field field : clazz.getDeclaredFields()) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                Value value = field.getAnnotation(Value.class);
                field.setAccessible(true);
                if (null != autowired){
                    try {
                        field.set(component, getInstance(field.getType(), autowired));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("无法向 ["+ clazz.getTypeName()+"] 的属性 [" + field.getName() +"] 注入依赖");
                    } catch (RuntimeException e){
                        throw new RuntimeException("无法向 ["+ clazz.getTypeName()+"] 的属性 [" + field.getName() +"] 注入依赖 因为 "+ e.getMessage());
                    }
                }else if (null != value && !"".equals(value.value())){
                    //注入配置文件的配置
                    try {
                        Object cast = field.getType().cast(properties.get(value.value()));
                        field.set(component, cast);
                    } catch (Exception e) {
                        throw new RuntimeException("无法向 ["+ clazz.getTypeName()+"] 的属性 [" + field.getName() +"] 注入配置值，请检查配置文件");
                    }
                }
            }
        });
    }


    /**
     * 加载配置文件
     * @param path
     */
    public Properties propertiesLoad(String path) {
        Properties properties = new Properties();
        InputStream inputStream = JwApplication.class.getClassLoader().getResourceAsStream(path);
        try {
            properties.load(inputStream);
            inputStream.close();
            return properties;
        } catch (IOException e) {
            System.err.println("加载配置文件失败");
            e.printStackTrace();
        }
        return null;
    }


    private Object getInstance(Class<?> clazz, Autowired autowired){
        //判断注解是否写了值
        if (!autowired.value().isAssignableFrom(Autowired.class)){
            return IocContainerContext.getInstance().getBeanByClass(autowired.value());
        }else {
            List<? extends Class<?>> sonClass = IocContainerContext.getInstance().getSonClass(clazz);
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
