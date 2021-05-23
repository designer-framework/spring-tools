package org.designer.handler.impl;

import org.designer.handler.ExceptionHandler;
import org.designer.thread.entity.JobInfo;
import org.designer.thread.entity.JobResult;
import org.designer.thread.exception.BaseJobException;
import org.designer.thread.exception.UnknownJobException;

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
        JobResult<J> jobResult = new JobResult<>(requestInfo);
        if (jobResult.getException() instanceof BaseJobException) {
            jobResult.exception(e);
        } else {
            jobResult.exception(new UnknownJobException(requestInfo.getJobId(), e));
        }
        return jobResult;
    }

}
