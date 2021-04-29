package org.designer.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.designer.common.bean.App;
import org.designer.common.classload.AppClassloader;
import org.designer.common.context.Context;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:07
 */
public class AppUtils {

    private static final String CLASS_PATH = "classpath:/";

    private static final String WEB_PATH = "web";

    private static final String APP_SETTING = "app.properties";

    @SneakyThrows
    public static <T> List<T> getApps(Class<T> obj) {
        ClassPathResource classPathResource = new ClassPathResource(WEB_PATH);
        File file = classPathResource.getFile();
        return Arrays.stream(file.listFiles()).map(s -> {
            File appSettingFile = new File(s, APP_SETTING);
            try (InputStream inputStream = new FileInputStream(appSettingFile)) {
                Properties properties = new Properties();
                properties.load(inputStream);
                Map<String, Object> appInfo = new HashMap<>();
                Enumeration<Object> elements = properties.keys();
                while (elements.hasMoreElements()) {
                    String k = String.valueOf(elements.nextElement());
                    Object v = properties.get(k);
                    appInfo.put(k, v);
                }
                appInfo.put("appPath", s.getAbsolutePath());
                appInfo.put("appName", s.getName());
                return new JSONObject(appInfo).toJavaObject(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public static Context reflectInvokeApp(String clazz, ClassLoader classLoader, App args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> loadClass = ((AppClassloader) classLoader).loadClass(clazz, true);
        Method main = loadClass.getDeclaredMethod("start", App.class);
        Object context = main.invoke(loadClass, args);
        if (context instanceof Context) {
            return (Context) context;
        } else {
            throw new IllegalStateException("加载的不是应用：" + args);
        }
    }

}
