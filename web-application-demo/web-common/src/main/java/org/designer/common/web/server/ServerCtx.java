package org.designer.common.web.server;

import org.designer.common.bean.App;
import org.designer.common.bean.RunStrategy;
import org.designer.common.context.Context;
import org.designer.common.web.util.HttpHandler;
import org.designer.common.web.util.SelectorHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/18 23:33
 */
public class ServerCtx {

    /**
     * 多个APP组成的上下文
     */
    private final Map<String, Context> appContexts;

    /**
     * 细粒度锁
     */
    private final Object lock = new Object();

    private volatile boolean started;

    public ServerCtx() {
        appContexts = new ConcurrentHashMap<>(4);
    }

    public void putAppContext(String appName, Context appContainer) {
        synchronized (lock) {
            Context context = appContexts.put(appName, appContainer);
            if (started && context != null) {
                refresh();
            }
        }
    }

    public void startServer(RunStrategy runStrategy) {
        synchronized (lock) {
            if (!started) {
                starter(runStrategy);
            } else {
                refresh();
            }
        }
    }


    private void starter(RunStrategy runStrategy) {
        SelectorHandler selectorHandler;
        switch (runStrategy) {
            case MANY_PORT:
                selectorHandler = new HttpHandler();
                appContexts.forEach((appName, context) -> {
                    selectorHandler.putAppContext(appName, context);
                    starter(selectorHandler, context.getAppInfo());

                });
                break;
            case ONE_PORT:
                selectorHandler = new HttpHandler();
                appContexts.forEach(selectorHandler::putAppContext);
                starter(selectorHandler);
                break;
            default:
                break;
        }
        started = true;
    }

    private void starter(SelectorHandler selectorHandler) {
        Server server = new Server(selectorHandler, 8080);
        server.setDaemon(true);
        server.start();
    }

    private void starter(SelectorHandler selectorHandler, App app) {
        Server server = new Server(selectorHandler, app.getPort(), app.getAppName());
        server.setDaemon(true);
        new Server(selectorHandler, app.getPort(), app.getAppName())
                .start();
    }

    private void refresh() {
        //TODO
    }

}
