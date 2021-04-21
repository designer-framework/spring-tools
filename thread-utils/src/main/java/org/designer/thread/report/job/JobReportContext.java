package org.designer.thread.report.job;

import org.designer.thread.report.ReportContext;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:05
 */
public interface JobReportContext<K, V> extends ReportContext<K, V> {

    /**
     * 讲任务处理结果提交至报告中
     *
     * @param tJobResult
     */
    void submit(V tJobResult);

}
