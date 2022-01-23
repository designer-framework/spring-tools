package org.designer.thread.context;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.designer.handler.ExceptionHandler;
import org.designer.thread.batch.BatchInfo;
import org.designer.thread.context.report.job.JobReportContext;
import org.designer.thread.context.report.job.JobReportContextImpl;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.exception.JobExistException;
import org.designer.thread.job.Job;
import org.designer.thread.job.JobInfo;
import org.designer.thread.job.JobResult;

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
            , Predicate<JobResult<T>> jobInterruptPredict
            , ExceptionHandler<JobInfo, JobResult<T>> exceptionHandler
            , boolean waitResult
    ) {
        super(threadPoolExecutor, jobInterruptPredict, 300000);
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
        //将待处理任务保存至waitProcessJobs map中
        Future<JobResult<T>> jobResultFuture = waitProcessJobs.putIfAbsent(
                job.getJobId()
                , executorCompletionService.submit(copyJobToTask(job))
        );
        //存在重复的任务
        if (jobResultFuture == null) {
            jobReportContext.submitReport(JobResult.exception(new JobExistException(job.getJobId() + ", 任务名字重复")));
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
                    return JobResult.exception(
                            new InterruptedException("因[" + batchInfo.getId() + "]批次任务已完成, 任务" + job.getJobId() + "被跳过!")
                    );
                    //任务未被挂起则正常执行
                } else {
                    JobResult<T> jobResult = job.getTask().call(interrupt);
                    //通过jobResult来决定是否挂起任务
                    if (jobInterruptPredict != null) {
                        completion();
                        //已有状态为Completion的任务, 是否继续执行剩下的任务(可能有的业务需求只要有任意一个任务被完成,则跳过其他所有待执行的任务,并直接返回状态为Completion的任务结果)
                        if (jobInterruptPredict.test(jobResult)) {
                            //修改全局任务状态修改为已挂起
                            if (interrupt.interrupt()) {
                                log.info("任务挂起成功, 批处理ID: {}, 任务ID: {}", batchInfo.getId(), job.getJobId());
                                //多线程情况下,可能会多个线程在完成任务后进入此代码块, 导致此现象原因是此处的代码块并未保证它的并发安全, 暂时认为没必要
                            } else {
                                log.warn("重复挂起任务, 批处理ID: {}, 当前任务ID: {} !", batchInfo.getId(), job.getJobId());
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
