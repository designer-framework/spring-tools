package org.designer.thread.job;

import lombok.Getter;
import lombok.Setter;
import org.designer.thread.callable.JobCallable;
import org.designer.thread.enums.JobStatus;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
@Setter
public abstract class AbstractJob<R> implements JobItem<R> {
    private final String jobId;
    private final String jobName;
    private final LocalDateTime createTime;
    private final JobCallable<JobResult<R>> task;
    private LocalDateTime endTime;
    private JobStatus jobStatus;

    public AbstractJob(
            JobCallable<JobResult<R>> task
            , String jobId
            , String jobName
    ) {
        this.jobName = jobName;
        this.task = task;
        this.jobId = jobId;
        createTime = LocalDateTime.now();
    }

    public void end() {
        endTime = LocalDateTime.now();
    }


}
