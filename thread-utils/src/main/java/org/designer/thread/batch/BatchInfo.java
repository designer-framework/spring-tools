package org.designer.thread.batch;

import org.designer.thread.job.Job;

import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/13 20:48
 */
public interface BatchInfo<T> extends Batch {

    List<Job<T>> getJobs();

}
