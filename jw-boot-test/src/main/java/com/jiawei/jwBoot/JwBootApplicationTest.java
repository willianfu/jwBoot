package com.jiawei.jwBoot;

import com.jiawei.jwboot.CommandLineRunner;
import com.jiawei.jwboot.JwApplication;
import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
public class JwBootApplicationTest implements CommandLineRunner {


    public static void main(String[] args) throws CannotCompileException, InstantiationException, NotFoundException, IllegalAccessException {
        //AopPointcutScan.aopInit(null, "com.jiawei.jwBoot.service.impl.TestServiceImpl");
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
