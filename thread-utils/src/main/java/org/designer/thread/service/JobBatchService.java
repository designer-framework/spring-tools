package org.designer.thread.service;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.context.JobContext;
import org.designer.thread.context.JobContextImpl;
import org.designer.thread.entity.Job;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.report.container.ReportContextContainer;

import java.util.List;

/**
 * @description: 批处理任务集合, 可动态监控. 但具体的监控逻辑需根据实际业务情况自行实现
 * @author: Designer
 * @date : 2021/4/21 1:42
 */
@Log4j2
public class JobBatchService<T> {

    private final ReportContextContainer<T> reportContextContainer = new ReportContextContainer<>();

    /**
     * 批量投放任务集合, 直接返回结果, 也可以直接放入异步线程池.
     *
     * @param jobs
     * @param batchName
     * @return
     * @throws Exception
     */
    public JobContext<JobStatus, T> batchProcess(List<Job<T>> jobs, String batchName) throws Exception {
        try (JobContextImpl<T> testContext = new JobContextImpl<>();) {
            reportContextContainer.put(batchName, testContext);
            try {
                jobs.forEach(testContext::submitJob);
                boolean success = testContext.pollJob(testContext.getJobQueueSize());
                if (success) {
                    log.debug(batchName + "任务完成");
                    return testContext;
                } else {
                    throw new RuntimeException(batchName + "任务失败");
                }
            } finally {
                reportContextContainer.remove(batchName);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private String getPercentage(String batchId) {
        return reportContextContainer.get(batchId).getPercentage(JobStatus.COMPLETION);
    }


}
