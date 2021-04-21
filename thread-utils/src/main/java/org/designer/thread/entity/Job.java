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
public class Job<T> implements Callable<JobResult<T>> {

    private final String jobName;

    private final LocalDateTime createTime;

    private final Callable<JobResult<T>> task;

    public Job(Callable<JobResult<T>> task, String jobName) {
        super();
        this.task = task;
        this.jobName = jobName;
        createTime = LocalDateTime.now();
    }

    @Override
    public JobResult<T> call() throws Exception {
        try {
            return task.call();
        } catch (Exception e) {
            JobResult<T> tJobResult = new JobResult<>(jobName);
            tJobResult.exception(e);
            return tJobResult;
        }
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobName='" + jobName + '\'' +
                ", createTime=" + createTime +
                '}';
    }

}
