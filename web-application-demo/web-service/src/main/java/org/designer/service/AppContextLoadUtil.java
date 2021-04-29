package org.designer.service;

import lombok.extern.log4j.Log4j2;
import org.designer.common.bean.App;
import org.designer.common.classload.AppClassloader;
import org.designer.common.context.Context;
import org.designer.common.exception.AppException;
import org.designer.common.exception.FatalException;
import org.designer.common.exception.LoadException;
import org.designer.common.utils.AppUtils;
import org.designer.common.web.server.Server;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:45
 */
@Log4j2
public class AppContextLoadUtil {

    private final Server server;

    public AppContextLoadUtil() {
        server = new Server();
    }

    /**
     * 多线程加载APP
     *
     * @param thread
     * @return
     */
    private static ExecutorService newExecutor(int thread) {
        //不建议该方式创建线程池, 这里为了简化代码才这样写
        //建议使用: ThreadPoolExecutor
        return Executors.newFixedThreadPool(thread);
    }

    private static Context loadApp(App app) {
        try {
            AppClassloader appClassloader = new AppClassloader(new File(app.getAppPath()));
            log.debug("APP[{}]加载成功", app.getAppName());
            return AppUtils.reflectInvokeApp(app.getMain(), appClassloader, app);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new LoadException(String.format("APP%s加载失败", app.getAppName()), e);
        }
    }

    public void loadApps(Map<String, App> contexts) {
        CountDownLatch countDownLatch = new CountDownLatch(contexts.size());
        ExecutorService executor = newExecutor(contexts.size());
        Map<String, Context> contextMap = new HashMap<>();
        try {
            contexts.forEach((appName, appContext) -> {
                executor.execute(() -> {
                    try {
                        Context context = loadApp(appContext);
                        synchronized (contextMap) {
                            contextMap.put(appName, context);
                        }
                        server.putAppContext(appName, context);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            });
            countDownLatch.await();
            log.debug("APP加载完成");
            server.startSelectorPolling(8080);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new FatalException("加载异常", e);
        } finally {
            executor.shutdown();
        }
    }

}
