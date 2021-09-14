package org.designer.thread.service;

import org.designer.thread.batch.BatchInfo;
import org.designer.thread.job.JobResult;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/15 0:42
 */
public interface JobBatch<T> {

    void batchProcess(
            BatchInfo<JobResult<T>> batchInfo
    ) throws Exception;

}
