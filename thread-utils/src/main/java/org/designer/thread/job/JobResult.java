package org.designer.thread.job;

import lombok.Getter;
import org.designer.thread.enums.JobStatus;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
public class JobResult<T> {

    private long processTime;

    private JobStatus jobStatus;

    private Exception exception;

    private String failedMessage;

    private T result;

    private JobResult() {
        jobStatus = JobStatus.SUBMIT;
    }

    public static <T> JobResult<T> completion(T result) {
        JobResult<T> jobResult = new JobResult<>();
        jobResult.jobStatus = JobStatus.COMPLETION;
        jobResult.result = result;
        return jobResult;
    }

    /**
     * 任务失败
     *
     * @param result
     * @return
     */
    public static <T> JobResult<T> failed(T result, String failedMessage) {
        JobResult<T> jobResult = new JobResult<>();
        jobResult.jobStatus = JobStatus.FAILED;
        jobResult.result = result;
        jobResult.failedMessage = failedMessage;
        return jobResult;
    }

    /**
     * 任务发生异常
     *
     * @param e
     * @return
     */
    public static <T> JobResult<T> exception(Exception e) {
        JobResult<T> jobResult = new JobResult<>();
        jobResult.jobStatus = JobStatus.EXCEPTION;
        jobResult.exception = e;
        return jobResult;
    }

    public static <T> JobResult<T> init() {
        JobResult<T> jobResult = new JobResult<>();
        jobResult.jobStatus = JobStatus.SUBMIT;
        return jobResult;
    }

    public boolean isException() {
        return jobStatus == JobStatus.EXCEPTION;
    }

    public boolean isFailed() {
        return jobStatus == JobStatus.FAILED;
    }

    public boolean isCompletion() {
        return jobStatus == JobStatus.COMPLETION;
    }

    public JobResult<T> processTime(long processTime) {
        this.processTime = processTime;
        return this;
    }

}
