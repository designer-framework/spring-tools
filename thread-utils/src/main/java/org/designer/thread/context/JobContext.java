package org.designer.thread.context;

import org.designer.thread.entity.JobResult;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface JobContext<K, V> extends Context<K, V, JobResult<V>>, AutoCloseable {

    /**
     * 符合特定要求的资源总数
     *
     * @return
     */
    int getCompletionCount();
}
