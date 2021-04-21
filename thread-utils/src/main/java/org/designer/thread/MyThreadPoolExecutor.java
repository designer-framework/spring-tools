package org.designer.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import org.designer.thread.property.ThreadPoolProperty;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:10
 */
@Log4j2
public class MyThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {

    public MyThreadPoolExecutor(ThreadPoolProperty threadPoolProperty) {
        super(threadPoolProperty.getCoreSize()
                , threadPoolProperty.getMaxSize(), threadPoolProperty.getKeepAlive()
                , TimeUnit.MILLISECONDS
                , new ArrayBlockingQueue<>(threadPoolProperty.getQueueCapacity(), true)
                , threadPoolProperty.getThreadFactory() != null ? threadPoolProperty.getThreadFactory() : new ThreadFactoryBuilder()
                        .setPriority(10)
                        .setDaemon(threadPoolProperty.isDaemon())
                        .setNameFormat(threadPoolProperty.getThreadName())
                        .build()
                , (r, executor) -> {
                    try {
                        Thread.sleep(15000 + new Random().nextInt(10));
                        executor.execute(r);
                    } catch (Exception e) {
                        log.error("线程疲惫" + executor.toString(), e);
                    }
                });
        log.debug("CPU线程数:{}, 工作核心线程数: {}, 工作最大线程数: {}"
                , Runtime.getRuntime().availableProcessors()
                , threadPoolProperty.getCoreSize()
                , threadPoolProperty.getMaxSize()
        );
    }

    public static MyThreadPoolExecutor getInstance(String threadName, int queueSize) {
        return new MyThreadPoolExecutor(ThreadPoolProperty.getInstance(threadName));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof InterruptCallable) {
        }
        return super.newTaskFor(callable);
    }

}
