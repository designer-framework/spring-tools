package org.designer.thread.context;

import org.designer.thread.enums.JobStatus;
import org.designer.thread.report.job.JobReportContext;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface JobProcessorContext<V> extends Context<V> {

    /**
     * 符合特定要求的资源总数
     *
     * @return
     */
    int getCompletionCount();

    /**
     * 当前批次实际待执行任务大小
     *
     * @return
     */
    int getJobQueueSize();

    JobReportContext<JobStatus, V> getJobReportContext();

    void start();

}
