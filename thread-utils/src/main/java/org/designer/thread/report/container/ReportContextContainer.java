package org.designer.thread.report.container;

import org.designer.thread.context.JobContext;
import org.designer.thread.enums.JobStatus;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 所有批处理任务上下文
 * @author: Designer
 * @date : 2021/4/21 13:17
 */
public class ReportContextContainer<T> extends ConcurrentHashMap<String, JobContext<JobStatus, T>> {

    private static final long serialVersionUID = 2364358113666210945L;

}
