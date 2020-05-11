package com.jiawei.jwboot.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class HttpUtil {

    public static void returnNotFound(HttpServletResponse response) {
        try {
            response.sendError(500, "Sorry!\n not found you url\n please check!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void returnParamBad(HttpServletResponse response, String message) {
        try {
            response.sendError(400, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writerToResponseJson(HttpServletResponse response, Object body) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JSON.toJSONString(body));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writerToResponseHtml(HttpServletResponse response, String body) {
        try {
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Http请求Json类型参数反序列化成实体类
     * @param request 请求
     * @param clazz 目标类
     * @param <T> 返回类型
     * @return 数据对象
     */
    public static <T> T getRequestBody(HttpServletRequest request, Class<T> clazz) {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        try {
            for (int i = 0; i < contentLength; ) {
                int readLength = request.getInputStream().read(buffer, i, contentLength - i);
                if (readLength == -1) {
                    break;
                }
                i += readLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return JSON.parseObject(buffer, clazz);
    }
}
