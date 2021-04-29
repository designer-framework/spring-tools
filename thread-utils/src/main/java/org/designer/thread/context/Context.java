package org.designer.thread.context;

import org.designer.thread.entity.Job;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface Context<K, V, R> {

    void submitJob(Job<V> job);

    boolean pollJob(int count) throws InterruptedException, ExecutionException;

    Map<String, List<R>> getExceptionInfo();

    /**
     * 百分比
     *
     * @param k
     * @return
     */
    String getPercentage(K k);

    /**
     * 当前批次有多少待执行任务
     *
     * @return
     */
    int getJobQueueSize();

}
