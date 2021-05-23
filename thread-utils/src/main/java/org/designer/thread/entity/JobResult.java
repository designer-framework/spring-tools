package org.designer.thread.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.designer.thread.enums.JobStatus;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
@SuperBuilder
public class JobResult<T> {

    private final String jobBatchId;

    private final String jobId;

    private final LocalDateTime startTime;

    private LocalDateTime endTime;

    private JobStatus jobStatus;

    private String errorMsg;

    private Exception exception;

    private T result;

    /*private Future<JobResult<T>> resultFuture;

    public JobResult(Future<JobResult<T>> task, String jobBatchId, String jobId) {
        this(jobBatchId, jobId);
        resultFuture = task;
    }*/

    /**
     * , JobStatus jobStatus
     *
     * @param jobInfo
     */
    public JobResult(JobInfo jobInfo) {
        jobBatchId = jobInfo.getBatchId();
        jobId = jobInfo.getJobId();
        startTime = jobInfo.getCreateTime();
    }

    public JobResult(String jobBatchId, String jobId) {
        this.jobBatchId = jobBatchId;
        this.jobId = jobId;
        startTime = LocalDateTime.now();
        jobStatus = JobStatus.SUBMIT;
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
        exception = e;
        jobStatus = JobStatus.EXCEPTION;
    }

}
