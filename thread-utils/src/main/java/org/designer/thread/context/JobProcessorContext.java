package org.designer.thread.context;

import org.designer.thread.context.report.job.JobReportContext;
import org.designer.thread.enums.JobStatus;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface JobProcessorContext<V> extends JobContext<V> {


    JobReportContext<JobStatus, V> getJobReportContext();

    void start();

}
