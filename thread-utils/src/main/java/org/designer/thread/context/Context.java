package org.designer.thread.context;

import org.designer.thread.entity.Job;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface Context<K, V, R> {

    /**
     * 提交任务给线程池
     *
     * @param job
     */
    void submitJob(Job<V> job);

    /**
     * 呈从线程池获取已完成的任务, 没有则返回空
     *
     * @param count
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    boolean pollJob(int count) throws InterruptedException, ExecutionException, TimeoutException;

    boolean pollJob(int count, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException;

    /**
     * 获取任务结果状态为EXCEPTION的数据
     *
     * @return
     */
    Map<String, List<R>> getExceptionInfo();

    /**
     * 获取状态为K的任务百分比
     *
     * @param k
     * @return
     */
    String getPercentage(K k);

    /**
     * 当前批次实际待执行任务大小
     *
     * @return
     */
    int getJobQueueSize();

    /**
     * 当前批次所有任务大小
     *
     * @return
     */
    int getJobSize();

}
