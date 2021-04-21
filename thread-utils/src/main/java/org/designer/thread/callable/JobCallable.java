package org.designer.thread.callable;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.designer.thread.entity.JobResult;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 14:39
 */
@Getter
@SuperBuilder
public class JobCallable<T> implements Callable<JobResult<T>> {

    private final String jobId;

    private final LocalDateTime createTime;

    private final Callable<JobResult<T>> task;

    private final String batchId;

    public JobCallable(Callable<JobResult<T>> task, String jobId, String batchId) {
        super();
        this.task = task;
        this.jobId = jobId;
        this.batchId = batchId;
        createTime = LocalDateTime.now();
    }

    @Override
    public JobResult<T> call() throws Exception {
        try {
            return task.call();
        } catch (Exception e) {
            JobResult<T> tJobResult = new JobResult<>(jobId);
            tJobResult.exception(e);
            return tJobResult;
        }
    }


}
