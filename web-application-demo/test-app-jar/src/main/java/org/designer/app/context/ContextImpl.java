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
        log.info(THREAD_NAME + ": 启动App中!");
        log.info(THREAD_NAME + ": APP类加载器: " + getClass().getClassLoader());
        log.info(THREAD_NAME + ": APP加载完成");
        ServerContext serverContext = new ServerContext(this);
        serverContext.loadRequestMapping();
        return serverContext;
    }

    public App getAppInfo() {
        return app;
    }

}
