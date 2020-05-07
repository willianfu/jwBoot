package com.jiawei.jwboot;

import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;
import com.jiawei.jwboot.utils.ObjectUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class JwApplication {

    private static Set<Class<?>> classes;

    private static Properties properties;

    /**
     * 启动容器，框架启动入口
     *
     * @param clazz
     * @param args
     * @return
     */
    public static void run(Class<?> clazz, String[] args) {
        ObjectUtil.notNull(clazz);
        classes = IocContainerContext.getInstance().iocInit();
        propertiesLoad("application.properties");

    }

    /**
     * 加载配置文件
     * @param path
     */
    public static void propertiesLoad(String path) {
        properties = new Properties();
        InputStream inputStream = JwApplication.class.getResourceAsStream(path);
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            System.err.println("加载配置文件失败");
            e.printStackTrace();
        }
    }
}
