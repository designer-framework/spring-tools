package org.designer.thread.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.designer.thread.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
@SuperBuilder
public class JobResult<T> {

    private final String jobName;

    private final LocalDateTime startTime;

    private LocalDateTime endTime;

    private JobStatus jobStatus;

    private String errorMsg;

    private Exception e;

    private T result;

    private Future<JobResult<T>> resultFuture;

    public JobResult(Future<JobResult<T>> task, String jobName) {
        this(jobName);
        resultFuture = task;
    }

    public JobResult(String jobName) {
        this.jobName = jobName;
        jobStatus = JobStatus.SUBMIT;
        startTime = LocalDateTime.now();
    }

    public void setResult(T result) {
        jobStatus = JobStatus.COMPLETION;
        this.result = result;
    }

    public boolean hasException() {
        return jobStatus == JobStatus.EXCEPTION;
    }

    public boolean hasError() {
        return jobStatus == JobStatus.ERROR;
    }

    public void end() {
        endTime = LocalDateTime.now();
    }

    public final void failed(String errorMsg) {
        this.errorMsg = errorMsg;
        jobStatus = JobStatus.ERROR;
    }

    public final void error(String errorMsg) {
        this.errorMsg = errorMsg;
        jobStatus = JobStatus.ERROR;
    }

    public final void exception(Exception e) {
        this.e = e;
        jobStatus = JobStatus.EXCEPTION;
    }

    @Override
    public String toString() {
        return "JobResult{" +
                "jobName='" + jobName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", jobStatus=" + jobStatus +
                ", errorMsg='" + errorMsg + '\'' +
                ", e=" + e +
                ", result=" + result +
                ", resultFuture=" + resultFuture +
                '}';
    }
}
