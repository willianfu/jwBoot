package com.jiawei.jwboot.utils;

import java.math.BigDecimal;
import java.text.ParseException;

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

    public static boolean isNotNull(Object obj){
        return null != obj;
    }

    public static boolean isEmptyStr(String str){
        return null == str || "".equals(str.trim());
    }

    /**
     * 类型转换
     * @param type 需要转换成的类型
     * @param value 待转换的对象
     * @return 转换后的值
     */
    public static Object typeCover(Class<?> type, Object value) {
        //先尝试直接转换
        try {
            return type.cast(value);
        } catch (Exception e) {
            //直接转换失败再进行parse
            try {
                String val = String.valueOf(value);
                switch (type.getSimpleName()){
                    case "Integer":
                        return Integer.parseInt(val);
                    case "Long":
                        return Long.parseLong(val);
                    case "String":
                        return val;
                    case "Double":
                        return Double.parseDouble(val);
                    case "BigDecimal":
                        return BigDecimal.valueOf(Double.parseDouble(val));
                    case "Boolean":
                        return Boolean.parseBoolean(val);
                    default: return val;
                }
            } catch (Exception ex) {
                //再失败就返回null
                return null;
            }
        }
    }

}
