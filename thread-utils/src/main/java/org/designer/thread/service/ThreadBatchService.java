package org.designer.thread.service;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.context.JobContextImpl;
import org.designer.thread.entity.Job;
import org.designer.thread.report.ReportForm;

import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:42
 */
@Log4j2
public class ThreadBatchService<T> {

    public ReportForm<T> batchProcess(List<Job<T>> jobs, String batchName) throws Exception {
        try (JobContextImpl<T> testContext = new JobContextImpl<>();) {
            jobs.forEach(testContext::submitJob);
            boolean success = testContext.pollJob(testContext.getJobQueueSize());
            if (success) {
                log.debug(batchName + "任务完成");
                return testContext.getReportForm();
            } else {
                throw new RuntimeException(batchName + "任务失败");
            }
        } catch (Exception e) {
            throw e;
        }
    }


}
