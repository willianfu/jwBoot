package com.jiawei.jwboot.utils;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class ObjectUtil {

    public static void notNull(Object obj){
        if (null == obj){
            throw new RuntimeException("被加载的资源不能为Null");
        }
    }

    public static boolean isEmptyStr(String str){
        return null == str || "".equals(str.trim());
    }

}
