package com.jiawei.jwBoot;

import com.jiawei.jwboot.CommandLineRunner;
import com.jiawei.jwboot.JwApplication;
import com.jiawei.jwboot.annotation.JwBootApplication;
import com.jiawei.jwboot.mvc.aop.AopPointcutScan;
import com.jiawei.jwboot.mvc.servlet.model.ModelAndView;
import com.jiawei.jwboot.mvc.servlet.model.View;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.util.Map;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
@JwBootApplication
public class JwBootApplicationTest implements CommandLineRunner {


    public static void main(String[] args) throws CannotCompileException, InstantiationException, NotFoundException, IllegalAccessException {
        AopPointcutScan.aopInit(null, "com.jiawei.jwBoot.service.impl.TestServiceImpl");
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
