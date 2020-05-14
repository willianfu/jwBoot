package com.jiawei.jwboot.server;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface WebServer {

    void start(Integer port, String contextPath);

    void shutdown();

}
