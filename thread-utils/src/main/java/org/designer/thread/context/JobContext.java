package org.designer.thread.context;

import org.designer.thread.job.Job;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface JobContext<V> {


    /**
     * 符合特定要求的资源总数
     *
     * @return
     */
    int getCompletionCount();

    /**
     * 当前批次实际待执行任务大小
     * 已将无效任务排除在外
     *
     * @return
     */
    int getWaitProcessJobSize();

    /**
     * 提交任务给线程池
     *
     * @param job
     */
    void submitJob(Job<V> job);


}
