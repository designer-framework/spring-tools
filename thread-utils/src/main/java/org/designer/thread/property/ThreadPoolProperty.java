package org.designer.thread.property;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:36
 */
@Getter
@Setter
@Builder
public class ThreadPoolProperty {


    /**
     * 核心线程数
     */
    private int coreSize;

    /**
     * 最大线程数
     */
    private int maxSize;

    /**
     * 缓冲队列大小
     */
    private int queueCapacity;


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
    private boolean daemon;

    /**
     * 线程守护
     */
    private String threadName;
    /**
     * 缓冲队列
     */
    private Queue<Runnable> workQueue;
    /**
     * 线程创建工厂
     */
    private ThreadFactory threadFactory;
    private RejectedExecutionHandler handler;

    public static ThreadPoolProperty getInstance(String threadName) {
        return ThreadPoolProperty.builder()
                .coreSize(Runtime.getRuntime().availableProcessors() * 2)
                .keepAlive(10000)
                .daemon(true)
                .maxSize((int) (Runtime.getRuntime().availableProcessors() * 2.5))
                .queueCapacity(3000)
                .threadName(threadName)
                .workQueue(new ArrayBlockingQueue<>(3000, false))
                .threadFactory(
                        new ThreadFactoryBuilder().setPriority(10).setNameFormat(threadName).setDaemon(true).build()
                )
                .build();
    }


}

