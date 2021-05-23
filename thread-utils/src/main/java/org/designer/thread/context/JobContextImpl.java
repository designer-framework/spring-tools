package org.designer.thread.context;

import lombok.extern.log4j.Log4j2;
import org.designer.handler.ExceptionHandler;
import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobInfo;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.exception.JobExistException;
import org.designer.thread.exception.JobRepeatInterruptException;
import org.designer.thread.report.job.JobReportContext;
import org.designer.thread.report.job.JobReportContextImpl;
import org.designer.thread.utils.MathUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:23
 */
@Log4j2
public class JobContextImpl<T> extends AbstractJobContext<T> {

    /**
     * 待处理任务集合
     */
    protected final Map<String, JobResult<T>> waitProcessJobs;
    /**
     * 任务完成情况报告
     */
    protected final JobReportContext<JobStatus, JobResult<T>> jobReportContext;

    private final ExceptionHandler<JobInfo, JobResult<T>> exceptionHandler;

    // private final Object lock = new Object();

    public JobContextImpl(String jobBatchName, int queueSize, Predicate<JobResult<T>> processorCompletionPredict, ExceptionHandler<JobInfo, JobResult<T>> exceptionHandler) {
        super(jobBatchName, queueSize, processorCompletionPredict);
        this.exceptionHandler = exceptionHandler;
        jobReportContext = new JobReportContextImpl<>();
        waitProcessJobs = new ConcurrentHashMap<>();
    }

    @Override
    public void submitJob(Job<T> job) {
        if (waitProcessJobs.containsKey(job.getJobId())) {
            JobResult<T> tJobResult = new JobResult<>(job);
            tJobResult.exception(new JobExistException(job.getJobId() + ", 任务名字重复"));
            jobReportContext.submitReport(tJobResult);
        } else {
            //Future<JobResult<T>> jobResultFuture = myExecutorCompletionService.submit(copyJobToTask(job));
            myExecutorCompletionService.submit(copyJobToTask(job));
            waitProcessJobs.put(job.getJobId(), new JobResult<>(job));
        }
    }

    /**
     * 对Job进行二次封装, 当返回的结果符合任务完成条件则将当前任务池状态设置为已挂起,其它新来的线程不进行运算直接返回
     *
     * @param job
     * @return
     */
    private Callable<JobResult<T>> copyJobToTask(Job<T> job) {
        Lock readLock = readWriteLock.readLock();
        return () -> {
            readLock.lock();
            try {
                //已经被挂起则直接返回
                if (interrupt.getInterrupt()) {
                    JobResult<T> objectJobResult = new JobResult<>(job);
                    objectJobResult.exception(new InterruptedException("任务" + job.getJobId() + "已被中断!"));
                    return objectJobResult;
                } else {
                    //未被挂起则继续执行
                    JobResult<T> jobResult = job.getTask().call(interrupt);
                    //任务执行结果是否为已完成
                    if (jobCompletionPredict != null && jobResult.getJobStatus() == JobStatus.COMPLETION) {
                        completion();
                        //已有状态为Completion的任务, 是否继续执行剩下的任务
                        if (jobCompletionPredict.test(jobResult)) {
                            //任务挂起失败同时目标资源获取成功
                            if (!interrupt.interrupt()) {
                                jobResult.exception(new JobRepeatInterruptException("资源获取成功, 但任务在此之前已经被挂起：" + job.getJobId()));
                                log.warn("任务在此之前已经被挂起, 任务ID: {}, 批处理ID: {}", job.getJobId(), job.getBatchId());
                                return jobResult;
                            } else {
                                log.info("任务处理完毕, 任务ID: {}, 批处理ID: {}", job.getJobId(), job.getBatchId());
                            }
                        }
                        return jobResult;
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
    public void submitReport(JobResult<T> tJobResult) {
        jobReportContext.submitReport(tJobResult);
    }

    @Override
    public String getPercentage(JobStatus jobStatus) {
        return MathUtils.computePercentage(jobReportContext.getSizeByKey(jobStatus), jobReportContext.size());
    }

    @Override
    public Map<String, List<JobResult<T>>> getExceptionInfo() {
        return jobReportContext.getExceptionInfo(JobStatus.EXCEPTION);
    }

    @Override
    public int getJobQueueSize() {
        return waitProcessJobs.size();
    }

    @Override
    public int getJobSize() {
        return jobReportContext.size();
    }

}
