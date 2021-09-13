package org.designer.thread.utils.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:23
 */
@Getter
@Setter
public class ExecutorCompletionServiceBuilder<V> {

    private ThreadPoolExecutor threadPoolExecutor;

    public ExecutorCompletionService<V> build(int queueCapacity) {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutorBuilder().build(UUID.randomUUID().toString(), queueCapacity);
        }
        return new ExecutorCompletionService<>(threadPoolExecutor, new ArrayBlockingQueue<>(queueCapacity));
    }

    /**
     * @param completionQueue         存放处理结果的队列
     * @param threadPoolQueueCapacity 线程池队列大小
     * @return
     */
    public ExecutorCompletionService<V> build(BlockingQueue<Future<V>> completionQueue, int threadPoolQueueCapacity) {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutorBuilder().build(UUID.randomUUID().toString(), threadPoolQueueCapacity);
        }
        return new ExecutorCompletionService<>(threadPoolExecutor, completionQueue);
    }

}
