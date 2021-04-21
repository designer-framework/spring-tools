package org.designer.thread.report;

import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:05
 */
public interface ReportForm<T> {

    /**
     * 获取结果报告
     *
     * @return
     */
    Report<JobStatus, JobResult<T>> getReport();

}
