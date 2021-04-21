package org.designer.thread.report;

import org.designer.thread.entity.JobResult;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:05
 */
public interface ReportOperate<T> {
    void submit(JobResult<T> tJobResult);
}
