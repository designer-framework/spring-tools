package org.designer.thread.service;

import lombok.extern.log4j.Log4j2;
import org.designer.handler.impl.JobCallableExceptionHandler;
import org.designer.thread.context.JobContext;
import org.designer.thread.context.JobContextImpl;
import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.report.container.ReportContextContainer;

import java.util.List;
import java.util.function.Predicate;

/**
 * @description: 批处理任务集合, 可动态监控. 但具体的监控逻辑需根据实际业务情况自行实现
 * @author: Designer
 * @date : 2021/4/21 1:42
 */
@Log4j2
public class JobBatchService<T> {

    private final int queueSize;

    private final ReportContextContainer<T> reportContextContainer;

    public JobBatchService() {
        queueSize = 3000;
        reportContextContainer = new ReportContextContainer<>();
    }

    /**
     * 批量投放任务集合, 直接返回结果, 也可以直接放入异步线程池.
     *
     * @param jobs
     * @param batchName
     * @return
     * @throws Exception
     */
    public JobContext<JobStatus, T> batchProcess(List<Job<T>> jobs, String batchName) throws Exception {
        return batchProcess(jobs, batchName, queueSize, null);
    }

    /**
     * 批量投放任务集合
     *
     * @param jobs                       任务集合
     * @param batchName                  该任务的批次名
     * @param queueSize                  线程池的线程全部繁忙后, 将会放入队列, 该参数表示队列大小
     * @param processorCompletionPredict 当该判断返回true时, 则其他线程全部中断. 返回false 或, 传null, 则不做任何操作
     * @return
     * @throws Exception
     */
    public JobContext<JobStatus, T> batchProcess(List<Job<T>> jobs, String batchName, int queueSize, Predicate<JobResult<T>> processorCompletionPredict) throws Exception {
        //JobCallableExceptionHandler用来决定是将异常包装成结果返回还是直接抛出异常将程序中断
        try (JobContext<JobStatus, T> testContext = new JobContextImpl<>(batchName, queueSize, processorCompletionPredict, new JobCallableExceptionHandler<>())) {
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
