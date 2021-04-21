package org.designer.thread.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

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

    private final Callable<JobResult<T>> task;

    private final String batchId;

    public Job(Callable<JobResult<T>> task, String jobId, String batchId) {
        super();
        this.task = task;
        this.jobId = jobId;
        this.batchId = batchId;
        createTime = LocalDateTime.now();
    }

}
