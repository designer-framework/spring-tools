package org.designer.thread.job;

import org.designer.thread.callable.JobCallable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/24 0:31
 */
public interface JobItem<R> extends Job<JobResult<R>> {

    @Override
    JobCallable<JobResult<R>> getTask();

}
