package org.designer.thread.property;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:23
 */
@Getter
@Setter
@Builder
public class CompletionServiceProperty<V> {
    private ThreadPoolExecutor threadPoolExecutor;
    private BlockingQueue<Future<V>> queue;
}
