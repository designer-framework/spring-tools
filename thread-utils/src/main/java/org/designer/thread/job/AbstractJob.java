package org.designer.thread.job;

import lombok.Getter;
import lombok.Setter;
import org.designer.thread.callable.JobCallable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
@Setter
public abstract class AbstractJob<R> implements JobItem<R> {
    private final String jobId;
    private final JobCallable<JobResult<R>> task;

    public AbstractJob(
            JobCallable<JobResult<R>> task
            , String jobId
    ) {
        this.task = task;
        this.jobId = jobId;
    }

}
