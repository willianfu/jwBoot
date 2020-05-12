package com.jiawei.jwBoot;

import com.jiawei.jwboot.CommandLineRunner;
import com.jiawei.jwboot.JwApplication;
import com.jiawei.jwboot.annotation.JwBootApplication;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
@JwBootApplication
public class JwBootApplicationTest implements CommandLineRunner {


    public static void main(String[] args) {
        JwApplication.run(JwBootApplicationTest.class, args);
    }

    /**
     * 应用启动完成后执行本方法
     *
     * @param args 方法参数
     */
    @Override
    public void run(String... args) {
        System.out.println("------------JwBoot应用已启动----------");
    }
}
