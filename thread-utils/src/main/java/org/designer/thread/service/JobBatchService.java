package org.designer.thread.service;

import lombok.extern.log4j.Log4j2;
import org.designer.handler.impl.JobCallableExceptionHandler;
import org.designer.thread.batch.BatchInfo;
import org.designer.thread.context.JobProcessorContext;
import org.designer.thread.context.JobProcessorContextImpl;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.job.JobResult;
import org.designer.thread.report.job.JobReportContext;
import org.designer.thread.utils.builder.ThreadPoolExecutorBuilder;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Predicate;

/**
 * @description: 批处理任务集合, 可动态监控. 但具体的监控逻辑需根据实际业务情况自行实现
 * @author: Designer
 * @date : 2021/4/21 1:42
 */
@Log4j2
public class JobBatchService<T> {

    /**
     * 批量投放任务集合
     *
     * @param batchInfo                  批任务对象
     * @param queueSize                  线程池的线程全部繁忙后, 将会放入队列, 该参数表示队列大小
     * @param processorCompletionPredict 当该判断返回true时, 则其他线程全部中断. 返回false 或, 传null, 则不做任何操作
     * @return
     * @throws Exception
     */
    public JobReportContext<JobStatus, JobResult<T>> batchProcess(
            BatchInfo<JobResult<T>> batchInfo
            , int queueSize
            , Predicate<JobResult<T>> processorCompletionPredict
    ) throws Exception {
        return batchProcess(batchInfo, queueSize, processorCompletionPredict, false);
    }

    public JobReportContext<JobStatus, JobResult<T>> batchProcess(
            BatchInfo<JobResult<T>> batchInfo
            , int queueSize
            , Predicate<JobResult<T>> processorCompletionPredict
            , boolean waitResult
    ) throws Exception {
        //JobCallableExceptionHandler用来决定是将异常包装成结果返回还是直接抛出异常将程序中断
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutorBuilder().build(batchInfo.getId(), queueSize);
        try {
            JobProcessorContext<JobResult<T>> context = new JobProcessorContextImpl<>(
                    batchInfo
                    , threadPoolExecutor
                    , processorCompletionPredict
                    , new JobCallableExceptionHandler<>()
            );
            context.start();
            JobReportContext<JobStatus, JobResult<T>> jobReportContext = context.getJobReportContext();
            jobReportContext.waitResult(waitResult);
            return jobReportContext;
        } catch (Exception e) {
            throw e;
        }
    }

}
