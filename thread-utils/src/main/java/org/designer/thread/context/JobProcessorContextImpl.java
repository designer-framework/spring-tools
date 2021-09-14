package org.designer.thread.context;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.designer.handler.ExceptionHandler;
import org.designer.thread.batch.BatchInfo;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.exception.JobExistException;
import org.designer.thread.job.Job;
import org.designer.thread.job.JobInfo;
import org.designer.thread.job.JobResult;
import org.designer.thread.report.job.JobReportContext;
import org.designer.thread.report.job.JobReportContextImpl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:23
 */
@Log4j2
public class JobProcessorContextImpl<T> extends AbstractJobProcessorContext<T> {

    /**
     * 待处理任务集合
     */
    protected final Map<String, Future<JobResult<T>>> waitProcessJobs;
    private final ExceptionHandler<JobInfo, JobResult<T>> exceptionHandler;
    private final BatchInfo<JobResult<T>> batchInfo;
    /**
     * 任务报告
     */
    private final JobReportContext<JobStatus, JobResult<T>> jobReportContext;
    private final boolean waitResult;

    public JobProcessorContextImpl(
            BatchInfo<JobResult<T>> batchInfo
            , ThreadPoolExecutor threadPoolExecutor
            , Predicate<JobResult<T>> processorCompletionPredict
            , ExceptionHandler<JobInfo, JobResult<T>> exceptionHandler
            , boolean waitResult
    ) {
        super(threadPoolExecutor, processorCompletionPredict, 300000);
        this.batchInfo = batchInfo;
        this.waitResult = waitResult;
        this.exceptionHandler = exceptionHandler;
        waitProcessJobs = new ConcurrentHashMap<>();
        jobReportContext = new JobReportContextImpl<>(batchInfo, this, completionQueue);
    }

    @Override
    public JobReportContext<JobStatus, JobResult<T>> getJobReportContext() {
        if (waitResult) {
            try {
                jobReportContext.waitResult();
            } catch (InterruptedException e) {
                log.error("同步获取任务结果失败", e);
                throw new RuntimeException("同步获取任务结果失败", e);
            }
        }
        return jobReportContext;
    }

    @Override
    public void submitJob(Job<JobResult<T>> job) {
        if (waitProcessJobs.containsKey(job.getJobId())) {
            JobResult<T> tJobResult = new JobResult<>();
            tJobResult.exception(new JobExistException(job.getJobId() + ", 任务名字重复"));
            //将待处理任务保存至map中
        } else {
            waitProcessJobs.put(job.getJobId(), executorCompletionService.submit(copyJobToTask(job)));
        }
    }

    /**
     * 对Job进行二次封装, 当返回的结果符合任务完成条件则将当前任务池状态设置为已挂起,其它新来的线程不进行运算直接返回
     *
     * @param job
     * @return
     */
    private Callable<JobResult<T>> copyJobToTask(Job<JobResult<T>> job) {
        Lock readLock = readWriteLock.readLock();
        return () -> {
            readLock.lock();
            try {
                //任务已经被挂起则直接保存处理结果
                if (interrupt.getInterrupt()) {
                    JobResult<T> jobResult = new JobResult<>();
                    jobResult.exception(new InterruptedException("因第" + batchInfo.getId() + "批次任务已完成, 任务" + job.getJobId() + "被跳过!"));
                    return jobResult;
                    //任务未被挂起则正常执行
                } else {
                    JobResult<T> jobResult = job.getTask().call(interrupt);
                    //任务执行结果是否为已完成
                    if (jobCompletionPredict != null && jobResult.getJobStatus() == JobStatus.COMPLETION) {
                        completion();
                        //已有状态为Completion的任务, 是否继续执行剩下的任务(可能有的业务需求只要有任意一个任务被完成,则跳过其他所有待执行的任务,并直接返回状态为Completion的任务结果)
                        if (jobCompletionPredict.test(jobResult)) {
                            //任务已完成, 修改全局任务状态为已挂起
                            //挂起成功
                            if (interrupt.interrupt()) {
                                log.info("任务处理完毕, 批处理ID: {}, 任务ID: {}", batchInfo.getId(), job.getJobId());
                                //任务已完成, 但挂起失败(多线程并发的情况下,可能会进入此代码块,因此处的代码块没必要也并未保证它的并发安全)
                                //挂起失败
                            } else {
                                log.warn("任务已完成, 但在此之前已有任务被完成! 批处理ID: {}, 当前任务ID: {} !", batchInfo.getId(), job.getJobId());
                            }
                        }
                    }
                    return jobResult;
                }
            } catch (Exception e) {
                return exceptionHandler.handler(e, job);
            } finally {
                readLock.unlock();
            }
        };
    }

    @Override
    public int getWaitProcessJobSize() {
        return waitProcessJobs.size();
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            List<Job<JobResult<T>>> jobs = batchInfo.getJobs();
            //提交任务至线程池
            jobs.forEach(this::submitJob);
            //启动任务报告线程
            jobReportContext.start();
        } finally {
            try {
                close();
            } catch (Exception e) {
                log.error("资源释放失败", e);
            }
        }
    }

}
