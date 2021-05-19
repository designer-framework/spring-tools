package org.designer.app.context;

import lombok.extern.log4j.Log4j2;
import org.designer.common.annotation.Controller;
import org.designer.common.bean.App;
import org.designer.common.context.AppContext;

/**
 * @description: 该类为测试类
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
@Controller
public class AppContextInfo {

    private final String THREAD_NAME = Thread.currentThread().getName();

    private final App app;

    private final ClassLoader classLoader;

    public AppContextInfo(App app) {
        this.app = app;
        classLoader = getClass().getClassLoader();
    }

    public AppContext startApp() {
        log.info(THREAD_NAME + ": 启动App[{}]中", app.getAppName());
        log.info(THREAD_NAME + ": APP[{}]类加载器: {}", app.getAppName(), classLoader);
        log.info(THREAD_NAME + ": APP[{}]加载完成!", app.getAppName());
        AppContextImpl appServerContext = new AppContextImpl(this);
        appServerContext.loadRequestMapping();
        return appServerContext;
    }

    public App getAppInfo() {
        return app;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

}
