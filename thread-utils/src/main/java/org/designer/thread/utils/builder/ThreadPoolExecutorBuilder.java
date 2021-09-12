package org.designer.thread.utils.builder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

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
     * 线程创建工厂
     */
    private ThreadFactory threadFactory;
    private RejectedExecutionHandler handler;

    private boolean fair;

    public ThreadPoolExecutor build(String threadName, int queueCapacity) {
        Assert.notNull(threadName, "threadName");
        Assert.isTrue(queueCapacity != 0, "queueCapacity");
        if (coreSize == 0) {
            coreSize = CORE_SIZE * 2;
        }
        if (keepAlive == 0) {
            keepAlive = 10000;
        }
        if (maxSize == 0) {
            maxSize = (int) (CORE_SIZE * 2.5);
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
                ,
                (r, executor) -> {
                    try {
                        Thread.sleep(15000 + new Random().nextInt(10));
                        executor.execute(r);
                    } catch (Exception e) {
                        log.error("线程疲惫" + executor.toString(), e);
                    }
                }
        );
    }

}

