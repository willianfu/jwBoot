package com.jiawei.jwboot.mvc.ioc;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface IocContainer {

    /**
     * 从容器中获取一个Bean
     * @param clazz bean类型
     * @return bean实例
     */
    <T> T getBeanByClass(Class<T> clazz);

    /**
     * 将一个实例对象放入ioc容器中
     * @param obj 对象
     */
    void putBeanToIoc(Object obj);

    /**
     * 从容器中移除一个实例
     * @param clazz bean类型
     * @param <T> 返回值泛型
     * @return 被移除的bean
     */
    <T> T removeBeanByClass(Class<T> clazz);
}
