package org.designer.thread.context;

import org.designer.thread.job.Job;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface Context<V> {

    /**
     * 提交任务给线程池
     *
     * @param job
     */
    void submitJob(Job<V> job);


}
