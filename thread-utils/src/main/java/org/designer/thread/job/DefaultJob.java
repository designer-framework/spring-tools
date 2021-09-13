package org.designer.thread.job;

import lombok.Getter;
import org.designer.thread.callable.JobCallable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
public class DefaultJob<T> extends AbstractJob<T> {

    public DefaultJob(JobCallable<JobResult<T>> task, String jobId, String jobName) {
        super(task, jobId, jobName);
    }


}
