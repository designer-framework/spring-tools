package org.designer.thread.job;

import org.designer.thread.callable.JobCallable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/24 0:31
 */
public interface Job<R> extends JobInfo {

    JobCallable<R> getTask();

}
