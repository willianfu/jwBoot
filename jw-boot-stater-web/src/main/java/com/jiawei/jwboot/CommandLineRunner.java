package com.jiawei.jwboot;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface CommandLineRunner {

    /**
     * 应用启动完成后执行本方法
     * @param args 方法参数
     */
    void run(String... args);
}
