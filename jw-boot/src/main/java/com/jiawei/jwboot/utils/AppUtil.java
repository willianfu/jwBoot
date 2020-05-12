package com.jiawei.jwboot.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : willian fu
 * @date : 2020/5/11
 */
public class AppUtil {

    public static String logo = "   ___         ______             _\n" +
                    "  |_  |        | ___ \\           | |\n" +
                    "    | |_      _| |_/ / ___   ___ | |_\n" +
                    "    | \\ \\ /\\ / / ___ \\/ _ \\ / _ \\| __|\n" +
                    "/\\__/ /\\ V  V /| |_/ / (_) | (_) | |_\n" +
                    "\\____/  \\_/\\_/ \\____/ \\___/ \\___/ \\__|\n" +
                    "     (version V1.0.0 by jiawei)\n";

    public static String getStartLogo(){
        InputStream inputStream = AppUtil.class.getClassLoader().getResourceAsStream("banner.txt");
        if (ObjectUtil.isNotNull(inputStream)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuilder builder = new StringBuilder();
            try {
                while (ObjectUtil.isNotNull(line = reader.readLine())){
                    builder.append(line).append("\n");
                }
                inputStream.close();
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logo;
    }

    public static Logger getLogger(Class<?> clazz){
        return LoggerFactory.getLogger(clazz);
    }

}
