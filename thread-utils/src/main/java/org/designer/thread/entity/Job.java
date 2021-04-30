package org.designer.thread.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.designer.thread.callable.JobCallable;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
@SuperBuilder
public class Job<T> {

    private final String jobId;

    private final LocalDateTime createTime;

    private final JobCallable<JobResult<T>> task;

    private final String batchId;

    public Job(JobCallable<JobResult<T>> task, String jobId, String batchId) {
        super();
        this.task = task;
        this.jobId = jobId;
        this.batchId = batchId;
        createTime = LocalDateTime.now();
    }

}
