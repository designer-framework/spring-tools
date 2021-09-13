package org.designer.thread.report.job;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.batch.BatchInfo;
import org.designer.thread.context.JobProcessorContext;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.exception.JobStatusException;
import org.designer.thread.job.JobResult;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @description: 任务处理报告上下文
 * @author: Designer
 * @date : 2021/4/20 23:10
 */
@Log4j2
public class JobReportContextImpl<T> extends Thread implements JobReportContext<JobStatus, JobResult<T>> {

    private final JobReportMap<JobStatus, JobResult<T>> jobReport = new JobReportMap<>();

    private final BatchInfo<JobResult<T>> batchInfo;

    private final BlockingQueue<Future<JobResult<T>>> completionData;
    private final JobProcessorContext<JobResult<T>> jobProcessorContext;
    private final CountDownLatch lock = new CountDownLatch(1);

    public JobReportContextImpl(
            BatchInfo<JobResult<T>> batchInfo
            , JobProcessorContext<JobResult<T>> jobProcessorContext
            , BlockingQueue<Future<JobResult<T>>> completionData
    ) {
        this.batchInfo = batchInfo;
        this.jobProcessorContext = jobProcessorContext;
        this.completionData = completionData;
    }

    @Override
    public void waitResult(boolean waitResult) throws InterruptedException {
        if (waitResult) {
            lock.await();
        }
    }

    @Override
    public boolean pollJob(int jobTotal) throws Exception {
        return pollJob(jobTotal, Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

    @Override
    public boolean pollJob(int jobTotal, long timeout, TimeUnit timeUnit) throws Exception {
        if (jobTotal <= 0) {
            return true;
        }
        //已处理完成的任务大小
        int jobIndex = 0;
        //当前线程轮询次数
        int pollCount = 0;
        //当前线程轮询时间
        long currentJobStartTime = System.currentTimeMillis();
        //任务是否已经处理完
        while (jobIndex != jobTotal) {
            if (pollCount > 0) {
                long gapMilliSeconds = timeUnit.convert(System.currentTimeMillis() - currentJobStartTime, TimeUnit.MILLISECONDS);
                if (gapMilliSeconds > timeout) {
                    throw new TimeoutException("任务获取超时");
                }
            } else {
                pollCount++;
            }
            Future<JobResult<T>> result;
            while ((result = completionData.poll()) != null) {
                try {
                    JobResult<T> tJobResult = result.get();
                    log.debug(tJobResult);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("tJobResult", e);
                } finally {
                    jobIndex++;
                    pollCount = 0;
                    currentJobStartTime = System.currentTimeMillis();
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
            success = pollJob(jobProcessorContext.getJobQueueSize());
            if (success) {
                log.debug(batchInfo.getId() + "任务完成");
            } else {
                throw new RuntimeException(batchInfo.getId() + "任务失败");
            }
        } catch (Exception e) {
            log.error("拉取结果失败", e);
        } finally {
            lock.countDown();
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
    public void submitReport(JobResult<T> tJobResult) {
        if (tJobResult.getJobStatus() == JobStatus.SUBMIT) {
            JobResult<T> result = new JobResult<>();
            result.exception(new JobStatusException("xxx"));
            jobReport.add(result.getJobStatus(), tJobResult);
        } else {
            jobReport.add(tJobResult.getJobStatus(), tJobResult);
        }
        tJobResult.end();
        log.debug(tJobResult.toString());
    }

}
