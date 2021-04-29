package org.designer.thread.callable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.designer.thread.entity.JobResult;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 14:39
 */
@Getter
@Setter
@Accessors(chain = true)
public class JobCallableImpl<T> implements Callable<JobResult<T>> {

    private final String jobId;
    private final Callable<JobResult<T>> task;
    private final String batchId;
    /**
     * 准备调用多线程执行任务的时间
     */
    private final LocalDateTime startTime;
    /**
     * 创建任务的时间
     */
    private LocalDateTime createTime;

    public JobCallableImpl(Callable<JobResult<T>> task, String jobId, String batchId) {
        super();
        this.jobId = jobId;
        this.batchId = batchId;
        this.task = task;
        createTime = LocalDateTime.now();
        startTime = LocalDateTime.now();
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
