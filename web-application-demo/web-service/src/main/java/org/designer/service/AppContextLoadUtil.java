package org.designer.service;

import lombok.extern.log4j.Log4j2;
import org.designer.common.bean.App;
import org.designer.common.bean.RunStrategy;
import org.designer.common.classload.AppClassloader;
import org.designer.common.context.AppContext;
import org.designer.common.exception.AppException;
import org.designer.common.exception.FatalException;
import org.designer.common.exception.LoadException;
import org.designer.common.utils.AppUtils;
import org.designer.common.web.server.ServerCtx;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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

    private final ServerCtx serverCtx;

    public AppContextLoadUtil() {
        serverCtx = new ServerCtx();
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

    private static AppContext loadApp(App app) {
        try {
            AppClassloader appClassloader = new AppClassloader(new File(app.getAppPath()));
            AppContext appContext = AppUtils.reflectInvokeApp(app.getMain(), appClassloader, app);
            log.debug("APP[{}]加载成功, 访问地址: {}, 可访问的路径: {}"
                    , app.getAppName()
                    , "http://127.0.0.1:" + app.getPort() + "/" + app.getAppName()
                    , appContext.getRequestMappingUrls());
            return appContext;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new LoadException(String.format("APP[%s]加载失败", app.getAppName()), e);
        }
    }

    /**
     * @param contexts
     * @param runStrategy 暂时只支持所有服务运行在相同的端口上
     */
    public void loadApps(Map<String, App> contexts, RunStrategy runStrategy) {
        CountDownLatch countDownLatch = new CountDownLatch(contexts.size());
        ExecutorService executor = newExecutor(contexts.size());
        try {
            contexts.forEach((appName, appContext) -> {
                executor.execute(() -> {
                    try {
                        AppContext context = loadApp(appContext);
                        serverCtx.putAppContext(appName, context);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            });
            countDownLatch.await();
            serverCtx.startServer(runStrategy);
            log.debug("Server启动完成");
            synchronized (this) {
                //测试
                wait();
            }
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
