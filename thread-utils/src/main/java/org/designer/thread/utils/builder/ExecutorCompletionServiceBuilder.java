package org.designer.thread.utils.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

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
@Accessors(chain = true)
public class ExecutorCompletionServiceBuilder<V> {

    private ThreadPoolExecutor threadPoolExecutor;

    private BlockingQueue<Future<V>> queue;

    public ExecutorCompletionService<V> build() {
        Assert.notNull(threadPoolExecutor, "threadPoolExecutor");
        Assert.notNull(queue, "queue");
        return new ExecutorCompletionService<>(threadPoolExecutor, queue);
    }

}
