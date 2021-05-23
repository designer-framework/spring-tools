package org.designer.thread.entity;

import org.designer.thread.callable.JobCallable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/24 0:31
 */
public interface BaseJob<T> {

    JobCallable<JobResult<T>> getTask();

}
