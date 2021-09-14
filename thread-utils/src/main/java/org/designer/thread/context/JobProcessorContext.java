package org.designer.thread.context;

import org.designer.thread.enums.JobStatus;
import org.designer.thread.report.job.JobReportContext;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface JobProcessorContext<V> extends JobContext<V> {


    JobReportContext<JobStatus, V> getJobReportContext();

    void start();

}
