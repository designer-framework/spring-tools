package org.designer.thread.context;

import org.designer.thread.entity.Job;
import org.designer.thread.report.ReportForm;

import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:34
 */
public interface JobContext<T> {

    void submitJob(Job<T> job);

    boolean pollJob(int count) throws InterruptedException, ExecutionException;

    ReportForm<T> getReportForm();

}
