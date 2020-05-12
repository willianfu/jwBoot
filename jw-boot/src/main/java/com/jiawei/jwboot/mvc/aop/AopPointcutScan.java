package com.jiawei.jwboot.mvc.aop;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;



/**
 * @author : willian fu
 * @version : 1.0
 */
public class AopPointcutScan{

    public static void aopInit(String packege, String clszz) throws IllegalAccessException, InstantiationException, CannotCompileException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        //导入包
        pool.importPackage(packege);
        CtClass cc = pool.get(clszz);
        // 实例化代理类工厂
        ProxyFactory factory = new ProxyFactory();
        //设置父类，ProxyFactory将会动态生成一个类，继承该父类
        factory.setSuperclass(cc.toClass());
        //设置过滤器，判断哪些方法调用需要被拦截
        factory.setFilter(m -> {
            System.out.println(m.getName());
            return m.getName().startsWith("he");
        });
        //创建代理类型
        Class<?> clazz = factory.createClass();
        //创建代理实例
        //Point proxy = (Point) clazz.newInstance();
        //设置代理处理方法
        ((ProxyObject) clazz.newInstance()).setHandler((self, thisMethod, proceed, args) -> {
            //实际情况可根据需求修改
            System.out.println(thisMethod.getName() + "被调用");
            try {
                //thisMethod为被代理方法 proceed为代理方法 self为代理实例 args为方法参数
                Object ret = proceed.invoke(self, args);
                System.out.println("返回值: " + ret);
                return ret;
            } finally {
                System.out.println(thisMethod.getName() + "调用完毕");
            }
        });
    }
}
