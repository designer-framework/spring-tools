package org.designer.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Project: spring-tools
 * @Package: org.designer.app.invoke
 * @Author: Designer
 * @CreateTime: 2021-04-24 21
 * @Description:
 */

public class MethodInvoke {

    private final Class<?> targetClass;

    private final Object targetObject;

    private final String method;

    private final Class<?> returnType;

    private final Class<?>[] parameterTypes;

    public MethodInvoke(String method, Class<?> targetClass, Object targetObject, Class<?> returnType, Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.returnType = returnType;
        this.method = method;
    }

    public Object invoke(String args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        MethodInvoker methodInvoker = new MethodInvoker();
        methodInvoker.setTargetClass(targetClass);
        methodInvoker.setTargetObject(targetObject);
        methodInvoker.setTargetMethod(method);
        methodInvoker.setArguments(converts(args));
        methodInvoker.prepare();
        return methodInvoker.invoke();
    }

    /**
     * 入参转换
     *
     * @param args
     * @return
     */
    public Object[] converts(String args) {
        List<Object> list = new LinkedList<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            Object convert = convert(parameterTypes[i], args);
            list.add(convert);
        }
        return list.toArray();
    }

    /**
     * 入参转换
     *
     * @param parameterType
     * @param args
     * @return
     */
    public Object convert(Class<?> parameterType, String args) {
        if (parameterType == String.class) {
            return args;
        } else {
            try {
                return JSON.parseObject(args, parameterType);
            } catch (Exception e) {
                throw new IllegalArgumentException("参数转换失败", e);
            }
        }
    }


}
