package org.designer.thread.utils.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:23
 */
@Getter
@Setter
public class ExecutorCompletionServiceBuilder<V> {

    /**
     * @param completionQueue         存放处理结果的队列
     * @param threadPoolQueueCapacity 线程池队列大小
     * @return
     */
    public ExecutorCompletionService<V> build(
            BlockingQueue<Future<V>> completionQueue
            , int threadPoolQueueCapacity
    ) {
        return new ExecutorCompletionService<>(
                new ThreadPoolExecutorBuilder().build(UUID.randomUUID().toString(), threadPoolQueueCapacity)
                , completionQueue
        );
    }

    /**
     * @param completionQueue         存放处理结果的队列
     * @param threadPoolQueueCapacity 线程池队列大小
     * @return
     */
    public ExecutorCompletionService<V> build(
            ThreadPoolExecutor threadPoolExecutor
            , BlockingQueue<Future<V>> completionQueue
            , int threadPoolQueueCapacity
    ) {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutorBuilder().build(UUID.randomUUID().toString(), threadPoolQueueCapacity);
        }
        return new ExecutorCompletionService<>(threadPoolExecutor, completionQueue);
    }

}
