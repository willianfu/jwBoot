package com.jiawei.jwboot.server;

import com.jiawei.jwboot.mvc.servlet.JwBootDispatchServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import utils.AppUtil;


/**
 * @author : willian fu
 * @version : 1.0
 */
public class TomcatWebServer implements WebServer{

    private static Logger log = AppUtil.getLogger(TomcatWebServer.class);

    private static Tomcat tomcatServer;

    @Override
    public void start(Integer port, String contextPath) {
        Tomcat tomcat = new Tomcat();
        tomcatServer = tomcat;
        tomcat.setPort(port);
        Context ctx = tomcat.addContext("/", System.getProperty("user.dir"));
        Tomcat.addServlet(ctx, "JwBootDispatchServlet", new JwBootDispatchServlet());
        ctx.addServletMapping("/*", "JwBootDispatchServlet");
        try {
            tomcat.start();
            log.info("Tomcat successful start on port [{}]", port);
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
            try {
                tomcat.stop();
            } catch (LifecycleException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Tomcat 启动失败");
        }

    }



    @Override
    public void shutdown() {
        if (null != tomcatServer){
            try {
                tomcatServer.destroy();
                tomcatServer.stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }
    }
}
