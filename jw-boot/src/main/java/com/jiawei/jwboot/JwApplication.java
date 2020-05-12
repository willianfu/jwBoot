package com.jiawei.jwboot;

import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.annotation.component.di.Value;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;
import com.jiawei.jwboot.server.TomcatWebServer;
import com.jiawei.jwboot.server.WebServer;
import com.jiawei.jwboot.utils.AppUtil;
import com.jiawei.jwboot.utils.ObjectUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Component
public class JwApplication  {

    private static Logger log = AppUtil.getLogger(JwApplication.class);

    private static Set<Class<?>> classes;

    @Value("server.port")
    private Integer port;

    @Value("server.context-path")
    private String rootPath;

    /**
     * 启动容器，框架启动入口
     *
     * @param clazz
     * @param args
     * @return
     */
    public static void run(Class<?> clazz, String[] args) {
        System.out.println("\033[0;33m" + AppUtil.getStartLogo() + "\033[0m ");
        ObjectUtil.notNull(clazz);
        classes = IocContainerContext.getInstance().iocInit();
        loadApplicationListener();
        IocContainerContext.getInstance().getBeanByClass(JwApplication.class).starterWebContainer();
    }

    private void starterWebContainer() {
        if (!ObjectUtil.isNotNull(port) || port == 0){
            port = 8080;
        }
        log.info("Initialize web server on port [{}]", port);
        WebServer server = new TomcatWebServer();
        server.start(port, rootPath);
    }

    private static void loadApplicationListener() {
        IocContainerContext instance = IocContainerContext.getInstance();
        List<Class<CommandLineRunner>> sonListener = instance.getSonClass(CommandLineRunner.class);
        sonListener.forEach(listener -> {
            instance.getBeanByClass(listener).run();
        });
    }

}
