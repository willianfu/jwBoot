package com.jiawei.jwboot;

import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;
import com.jiawei.jwboot.utils.ObjectUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Set;

/**
 * @author : willian fu
 * @version : 1.0
 */
@WebListener
public class JwApplication implements ServletContextListener {

    private static Set<Class<?>> classes;

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
        loadApplicationListener();
    }


    private static void loadApplicationListener(){
        IocContainerContext instance = IocContainerContext.getInstance();
        List<Class<CommandLineRunner>> sonListener = instance.getSonClass(CommandLineRunner.class);
        sonListener.forEach(listener -> {
            instance.getBeanByClass(listener).run();
        });
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("————————————————tomcat 启动完成——————————————————");
        JwApplication.run(JwApplication.class, null);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
