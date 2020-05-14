package bean.ioc;




import annotation.component.Component;
import bean.di.ComponentsDependConfig;
import org.reflections.Reflections;
import org.slf4j.Logger;
import utils.AppUtil;
import utils.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : willian fu
 * @version : 1.0
 */

public class IocContainerContext extends AbstractIocContext implements IocContainer {

    private static Logger log = AppUtil.getLogger(IocContainerContext.class);

    /**
     * 从容器中获取一个Bean
     *
     * @param clazz bean类型
     * @return bean实例
     */
    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        Object o = IOC_CONTAINER.get(clazz);
        return clazz.cast(IOC_CONTAINER.get(clazz));
    }

    public Map<Class, Object> getIocInstance(){
        return IOC_CONTAINER;
    }

    /**
     * 获取被扫描到的带有特定注解的类
     * @return 类class
     */
    public Map<Class, Object> getIocClassByAnnotation(Class<? extends Annotation> annotation){
        Map<Class, Object> clazzs =  new ConcurrentHashMap();
        IOC_CONTAINER.forEach((clazz, instance) ->{
            if (ObjectUtil.isNotNull(clazz.getAnnotation(annotation))){
                clazzs.put(clazz, instance);
            }
        });
        return clazzs;
    }
    /**
     * 将一个实例对象放入ioc容器中
     *
     * @param obj 对象
     */
    @Override
    public void putBeanToIoc(Object obj) {
        if (null == this.getBeanByClass(obj.getClass())) {
            IOC_CONTAINER.put(obj.getClass(), obj);
        } else {
            throw new RuntimeException("该实例已被IOC托管，无法覆盖");
        }
    }

    @Override
    public Set<Class<?>> iocInit() {
        //扫描所有的包
        Reflections reflections = new Reflections();;
        Set<Class<?>> component = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> classComponent = reflections.getTypesAnnotatedWith(Component.class);
        IocContainerContext instance = IocContainerContext.getInstance();
        for (Class<?> clazz : component) {
            //加载组件
            if (isClass(clazz)) {
                log.info("Scan to class [{}]", clazz.getTypeName());
                try {
                    Object instance1 = clazz.newInstance();
                    instance.putBeanToIoc(instance1);
                    classComponent.add(clazz);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("IOC初始化时，实例化类 [" + clazz.getTypeName() + "] 失败");
                }
            }
        }
        this.getBeanByClass(ComponentsDependConfig.class).init(IOC_CONTAINER);
        return classComponent;
    }

    /**
     * 获取某个类或接口的所有实现类
     * @param clazz 类型class
     * @return 类列表
     */
    @Override
    public <T> List<Class<T>> getSonClass(Class<T> clazz){
        List<Class<T>> classes = new ArrayList<>();
        IOC_CONTAINER.forEach((claz, component) ->{
            //取接口的所有子类
            if (clazz.isAssignableFrom(claz)){
                classes.add(claz);
            }
        });
        return classes;
    }

    private static boolean isClass(Class<?> clazz) {
        return !(clazz.isInterface() || clazz.isAnnotation());
    }

    /**
     * 从容器中移除一个实例
     *
     * @param clazz bean类型
     * @return 被移除的bean
     */
    @Override
    public <T> T removeBeanByClass(Class<T> clazz) {
        return clazz.cast(IOC_CONTAINER.get(clazz));
    }

    private static boolean has = false;

    private IocContainerContext() {
        synchronized (IocContainerContext.class) {
            //防止通过反射实例化
            if (!has) {
                has = true;
            } else {
                throw new RuntimeException("强制单例，不允许再次实例化");
            }
        }
    }

    public synchronized static IocContainerContext getInstance() {
        return Builder.INSTANCE;
    }

    private static final class Builder {
        private static final IocContainerContext INSTANCE = new IocContainerContext();
    }
}
