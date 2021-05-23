package org.designer.thread.entity;

import lombok.Getter;
import org.designer.thread.callable.JobCallable;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
public class DefaultJobInfo<T> extends AbstractJobInfo implements Job<T> {

    private final JobCallable<JobResult<T>> task;

    public DefaultJobInfo(JobCallable<JobResult<T>> task, String jobId, String batchId) {
        super(batchId, jobId, LocalDateTime.now());
        this.task = task;
    }


}
