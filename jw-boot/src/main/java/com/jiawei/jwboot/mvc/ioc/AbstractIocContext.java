package com.jiawei.jwboot.mvc.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : willian fu
 * @version : 1.0
 * IOC容器
 */
abstract class AbstractIocContext {

    protected static final Map<Class, Object> IOC_CONTAINER = new ConcurrentHashMap();

}
