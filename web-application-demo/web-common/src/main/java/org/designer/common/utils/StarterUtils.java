package org.designer.common.utils;

import org.designer.common.bean.App;
import org.designer.common.exception.FatalException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:49
 */
public class StarterUtils {

    public static <T extends App> Map<String, App> listToMap(List<T> apps) {
        return apps.stream()
                .collect(Collectors.toMap(App::getAppName, contextInfo -> contextInfo, (old, news) -> news));
    }

    public static void run(Class<?> clazz, String[] args) {
        try {
            Class<?> appClass = Class.forName(clazz.getName(), false, clazz.getClassLoader());
            callMain(appClass.getDeclaredConstructor().newInstance(), args);
        } catch (Exception e) {
            throw new FatalException("APP加载失败");
        }
    }

    private static void callMain(Object app, String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> aClass = app.getClass();
        Method mainMethod = aClass.getMethod("main", new Class[]{String[].class});
        mainMethod.invoke(aClass, new Object[]{args});
    }

}
