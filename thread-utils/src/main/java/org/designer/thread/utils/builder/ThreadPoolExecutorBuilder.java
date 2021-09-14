package org.designer.thread.utils.builder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:36
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class ThreadPoolExecutorBuilder {

    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程数
     */
    private int coreSize;

    /**
     * 最大线程数
     */
    private int maxSize;
    /**
     * 超时时间
     */
    private boolean allowCoreThreadTimeout;

    /**
     * 线程空闲存活时间 MS
     */
    private int keepAlive;

    /**
     * 线程守护
     */
    private boolean daemon = true;

    /**
     * 缓冲队列
     */
    private Queue<Runnable> workQueue;

    /**
     * 线程拒绝策略
     */
    private RejectedExecutionHandler rejectedHandler;

    private boolean fair;

    public ThreadPoolExecutor build(String threadName, int queueCapacity) {
        Assert.notNull(threadName, "线程名字不能为空");
        Assert.isTrue(queueCapacity != 0, "缓冲队列大小不能为空");
        if (coreSize == 0) {
            coreSize = CORE_SIZE;
        }
        if (keepAlive == 0) {
            keepAlive = 10000;
        }
        if (maxSize == 0) {
            maxSize = CORE_SIZE * 2 + 1;
        }
        log.debug("CPU线程数:{}, 工作核心线程数: {}, 工作最大线程数: {}"
                , CORE_SIZE
                , coreSize
                , maxSize
        );
        return new ThreadPoolExecutor(
                coreSize
                , maxSize
                , keepAlive
                , TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(queueCapacity, fair)
                , new ThreadFactoryBuilder()
                .setPriority(10)
                .setNameFormat(threadName)
                .setDaemon(daemon)
                .build()
                , rejectedHandler == null ? new ThreadPoolExecutor.CallerRunsPolicy() : rejectedHandler
        );
    }

}

