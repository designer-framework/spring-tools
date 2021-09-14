package org.designer.thread.job;

import lombok.Getter;
import org.designer.thread.enums.JobStatus;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
public class JobResult<T> {

    private final LocalDateTime startTime;

    private LocalDateTime endTime;

    private JobStatus jobStatus;

    private String errorMsg;

    private Exception exception;

    private T result;

    public JobResult() {
        startTime = LocalDateTime.now();
        jobStatus = JobStatus.SUBMIT;
    }

    public JobResult<T> completion(T result) {
        jobStatus = JobStatus.COMPLETION;
        this.result = result;
        return this;
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

    public final JobResult<T> error(String errorMsg) {
        this.errorMsg = errorMsg;
        jobStatus = JobStatus.ERROR;
        return this;
    }

    public final JobResult<T> exception(Exception e) {
        exception = e;
        jobStatus = JobStatus.EXCEPTION;
        return this;
    }

}
