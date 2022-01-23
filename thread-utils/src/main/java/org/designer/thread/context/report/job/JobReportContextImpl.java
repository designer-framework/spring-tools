package org.designer.thread.context.report.job;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.batch.BatchInfo;
import org.designer.thread.context.JobContext;
import org.designer.thread.context.JobProcessorContext;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.job.JobResult;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @description: 任务处理报告上下文
 * @author: Designer
 * @date : 2021/4/20 23:10
 */
@Log4j2
public class JobReportContextImpl<T> extends Thread implements JobReportContext<JobStatus, JobResult<T>> {

    public final CountDownLatch resultLock = new CountDownLatch(1);
    /**
     * 任务结果
     */
    private final JobReportMap<JobStatus, JobResult<T>> jobReport = new JobReportMap<>();
    /**
     * 批任务信息
     */
    private final BatchInfo<JobResult<T>> batchInfo;
    /**
     * 拉取结果的超时时间, 秒为单位
     */
    private final int pollJobTimeOut;
    /**
     * 存放完成后的任务
     */
    private final BlockingQueue<Future<JobResult<T>>> completionQueue;
    /**
     * 任务上下文
     */
    private final JobContext<JobResult<T>> jobContext;

    public JobReportContextImpl(
            BatchInfo<JobResult<T>> batchInfo
            , JobProcessorContext<JobResult<T>> jobContext
            , BlockingQueue<Future<JobResult<T>>> completionQueue
    ) {
        this.batchInfo = batchInfo;
        this.jobContext = jobContext;
        this.completionQueue = completionQueue;
        pollJobTimeOut = 2;
    }

    @Override
    public boolean pollJob(int jobTotal) throws Exception {
        return pollJob(jobTotal, pollJobTimeOut, TimeUnit.SECONDS);
    }

    @Override
    public boolean pollJob(int jobTotal, long timeout, TimeUnit timeUnit) throws Exception {
        //已处理完成的任务大小
        int jobIndex = 0;
        //任务是否已经处理完
        Assert.isTrue(jobTotal > 0, "任务数量不合法：" + jobTotal);
        while (jobIndex < jobTotal) {
            Future<JobResult<T>> result;
            while ((result = completionQueue.poll(timeout, TimeUnit.SECONDS)) != null) {
                try {
                    JobResult<T> tJobResult = result.get();
                    log.debug(tJobResult);
                } catch (Exception e) {
                    log.error("tJobResult", e);
                    jobReport.add(JobStatus.EXCEPTION, JobResult.exception(e));
                } finally {
                    jobIndex++;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return jobReport.toString();
    }

    @Override
    public int getSizeByKey(JobStatus jobStatus) {
        return jobReport.getSizeByKey(jobStatus);
    }


    @Override
    public void run() {
        boolean success;
        try {
            if (jobContext.getWaitProcessJobSize() <= 0) {
                log.error("没有要处理的任务!" + batchInfo);
                return;
            }
            success = pollJob(jobContext.getWaitProcessJobSize());
            if (success) {
                log.debug("任务完成" + batchInfo);
            } else {
                throw new RuntimeException("任务失败" + batchInfo);
            }
        } catch (Exception e) {
            log.error("拉取结果失败", e);
        } finally {
            resultLock.countDown();
        }
    }

    @Override
    public Map<String, List<JobResult<T>>> getExceptionInfo(JobStatus jobStatus) {
        List<JobResult<T>> jobByStatus = getJobByStatus(JobStatus.EXCEPTION);
        if (jobByStatus != null) {
            LinkedMultiValueMap<String, JobResult<T>> map = new LinkedMultiValueMap<>();
            jobByStatus.forEach(tJobResult -> {
                if (tJobResult.getException() != null) {
                    map.add(tJobResult.getException().getClass().getName(), tJobResult);
                } else {
                    map.add("", tJobResult);
                }
            });
            return map;
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    public List<JobResult<T>> getJobByStatus(JobStatus jobStatus) {
        return jobReport.get(jobStatus);
    }

    @Override
    public int size() {
        return jobReport.size();
    }

    @Override
    public JobReport<JobStatus, JobResult<T>> waitResult() throws InterruptedException {
        resultLock.await();
        return this;
    }

    @Override
    public void submitReport(JobResult<T> tJobResult) {
        jobReport.add(tJobResult.getJobStatus(), tJobResult);
        log.debug(tJobResult.toString());
    }

}
