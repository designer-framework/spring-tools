package org.designer.handler.impl;

import org.designer.handler.ExceptionHandler;
import org.designer.thread.exception.BaseJobException;
import org.designer.thread.exception.UnknownJobException;
import org.designer.thread.job.JobInfo;
import org.designer.thread.job.JobResult;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/23 23:21
 */
public class JobCallableExceptionHandler<J> implements ExceptionHandler<JobInfo, JobResult<J>> {

    public JobCallableExceptionHandler() {
    }

    @Override
    public JobResult<J> handler(Exception e, JobInfo requestInfo) {
        JobResult<J> jobResult = new JobResult<>();
        if (jobResult.getException() instanceof BaseJobException) {
            jobResult.exception(e);
        } else {
            jobResult.exception(new UnknownJobException(requestInfo.getJobId(), e));
        }
        return jobResult;
    }

}
