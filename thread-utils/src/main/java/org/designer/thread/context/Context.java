package org.designer.thread.context;

import org.designer.thread.entity.Job;

import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface Context<K, V> {


    void submitJob(Job<V> job);

    boolean pollJob(int count) throws InterruptedException, ExecutionException;

    /**
     * 百分比
     *
     * @param k
     * @return
     */
    String getPercentage(K k);


}
