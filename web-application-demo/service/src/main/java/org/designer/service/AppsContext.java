package org.designer.service;

import lombok.extern.log4j.Log4j2;
import org.designer.common.bean.AppContext;
import org.designer.common.classload.AppClassloader;
import org.designer.common.exception.AppException;
import org.designer.common.exception.FatalException;
import org.designer.common.exception.LoadException;
import org.designer.common.utils.AppUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:45
 */
@Log4j2
public class AppsContext {

    private final Object CONTEXT_LOCK = new Object();

    private Map<String, AppContext> contexts;

    /**
     * 多线程加载APP
     *
     * @param thread
     * @return
     */
    private static ExecutorService newExecutor(int thread) {
        return Executors.newFixedThreadPool(thread);
    }

    public static void loadApps(Map<String, AppContext> contexts) {
        CountDownLatch countDownLatch = new CountDownLatch(contexts.size());
        ExecutorService executor = newExecutor(contexts.size());
        try {
            contexts.forEach((s, appContext) -> {
                executor.execute(() -> {
                    try {
                        loadApp(appContext);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            });
            countDownLatch.await();
            log.debug("APP加载完成");
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

    private static void loadApp(AppContext appContext) {
        try {
            AppClassloader appClassloader = new AppClassloader(new File(appContext.getAppPath()));
            AppUtils.reflectInvokeApp(appContext.getMain(), appClassloader, appContext);
            log.debug("APP[{}]加载成功", appContext.getAppName());
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new LoadException(String.format("APP%s加载失败", appContext.getAppName()), e);
        }
    }

    public void pusAppInfo(Map<String, AppContext> contextInfoMap) {
        synchronized (CONTEXT_LOCK) {
            if (contexts == null) {
                contexts = new ConcurrentHashMap<>(4);
            }
            contexts.putAll(contextInfoMap);
        }
    }

}
