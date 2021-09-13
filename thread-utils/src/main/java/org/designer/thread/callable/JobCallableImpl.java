package org.designer.thread.callable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.designer.thread.job.JobInfo;
import org.designer.thread.job.JobResult;

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

    private final JobInfo jobInfo;
    private final Callable<JobResult<T>> task;
    /**
     * 准备调用多线程执行任务的时间
     */
    private final LocalDateTime startTime;
    /**
     * 创建任务的时间
     */
    private LocalDateTime createTime;

    public JobCallableImpl(Callable<JobResult<T>> task, JobInfo jobInfo) {
        super();
        this.jobInfo = jobInfo;
        this.task = task;
        createTime = LocalDateTime.now();
        startTime = LocalDateTime.now();
    }

    @Override
    public JobResult<T> call() throws Exception {
        return task.call();
    }


}
