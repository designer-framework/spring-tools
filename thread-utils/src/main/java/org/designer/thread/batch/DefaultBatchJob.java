package org.designer.thread.batch;

import lombok.Getter;
import lombok.Setter;
import org.designer.thread.job.Job;

import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/24 0:31
 */
@Getter
@Setter
public class DefaultBatchJob<T> implements BatchInfo<T> {

    private final String id;

    private final String name;

    private final List<Job<T>> jobs;

    public DefaultBatchJob(
            String id
            , String name
            , List<Job<T>> jobs
    ) {
        this.id = id;
        this.name = name;
        this.jobs = jobs;
    }

}
