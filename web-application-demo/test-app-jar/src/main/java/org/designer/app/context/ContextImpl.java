package org.designer.app.context;

import lombok.extern.log4j.Log4j2;
import org.designer.common.annotation.Controller;
import org.designer.common.bean.App;
import org.designer.common.context.Context;

/**
 * @description: 该类为测试类
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
@Controller
public class ContextImpl {

    private final String THREAD_NAME = Thread.currentThread().getName();

    private final App app;

    public ContextImpl(App app) {
        this.app = app;
    }

    public Context startApp() {
        log.info(THREAD_NAME + ": 启动App[{}]中", app.getAppName());
        log.info(THREAD_NAME + ": APP[{}]类加载器: {}", app.getAppName(), getClass().getClassLoader());
        log.info(THREAD_NAME + ": APP[{}]加载完成!", app.getAppName());
        AppServerContext appServerContext = new AppServerContext(this);
        appServerContext.loadRequestMapping();
        return appServerContext;
    }

    public App getAppInfo() {
        return app;
    }

}
