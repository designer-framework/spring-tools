package org.designer.thread;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.property.CompletionServiceProperty;

import java.util.concurrent.ExecutorCompletionService;

/**
 * @description: 线程池封装
 * @author: Designer
 * @date : 2021/3/7 22:18
 */
@Log4j2
public class MyExecutorCompletionService<V> extends ExecutorCompletionService<V> {

    public MyExecutorCompletionService(CompletionServiceProperty<V> completionServiceProperty) {
        super(
                completionServiceProperty.getThreadPoolExecutor()
                , completionServiceProperty.getQueue()
        );
    }

    public static <T> MyExecutorCompletionService<T> getInstance(CompletionServiceProperty<T> completionServiceProperty) {
        return new MyExecutorCompletionService<>(completionServiceProperty);
    }

}
