package org.designer.app.context;

import lombok.extern.log4j.Log4j2;
import org.designer.app.controller.TestController;
import org.designer.common.annotation.Controller;
import org.designer.common.annotation.RequestMapping;
import org.designer.common.context.Context;
import org.designer.common.exception.FatalException;
import org.designer.common.utils.MethodInvoke;
import org.designer.common.utils.UrlUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 该类为测试类
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
@Controller
public class ServerContext implements Context {

    private final String THREAD_NAME = Thread.currentThread().getName();

    private final Map<String, MethodInvoke> methodInvokes = new ConcurrentHashMap<>();

    private final ContextImpl context;


    public ServerContext(ContextImpl context) {
        this.context = context;
    }

    @Override
    public boolean support(String appName) {
        return context.getAppInfo().getAppName().equals(appName);
    }

    @Override
    public MethodInvoke getAppRequestMapping(String path) {
        return methodInvokes.get(path);
    }

    @RequestMapping("/getClassLoader")
    public String getClassLoader() {
        return getClass().getClassLoader().toString();
    }

    /**
     * 收据所有带注解的方法
     * Spring是利用bean加载时, 通过生命周期管理接口对bean进行解析
     */
    public void loadRequestMapping() {
        List<Class<?>> classes = scanJarsClass();
        classes.forEach(targetClass -> {
            ReflectionUtils.doWithMethods(targetClass, method -> {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                try {
                    Object targetObject = targetClass.getDeclaredConstructor().newInstance();

                    methodInvokes.put(UrlUtils.paddingUriPrefix(requestMapping.value()), toMethodInvoke(method, targetObject));
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    throw new FatalException("类加载失败: " + targetClass.getName(), e);
                }
                log.info(THREAD_NAME + ": RequestMapping[ " + requestMapping.value() + " ]");
            }, method -> method.isAnnotationPresent(RequestMapping.class) && method.getDeclaringClass().isAnnotationPresent(Controller.class));
        });
    }

    private List<Class<?>> scanJarsClass() {
        return Arrays.asList(TestController.class);
    }

    private MethodInvoke toMethodInvoke(Method method, Object targetObject) {
        return new MethodInvoke(
                method.getName()
                , method.getDeclaringClass()
                , targetObject
                , method.getReturnType()
                , method.getParameterTypes()
        );
    }

}
