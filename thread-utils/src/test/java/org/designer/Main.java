package org.designer;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.batch.BatchInfo;
import org.designer.thread.batch.DefaultBatchJob;
import org.designer.thread.context.report.job.JobReport;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.job.DefaultJob;
import org.designer.thread.job.Job;
import org.designer.thread.job.JobResult;
import org.designer.thread.service.impl.JobBatchService;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:14
 */
@Log4j2
public class Main {

    public static Job<JobResult<String>> newTask(String jobId) {
        return new DefaultJob<>((baseInterrupt) -> {
            int random = new Random().nextInt(1000);
            Thread.sleep(new Random().nextInt(2) * 1000);
            if (random % 101 == 0) {
                //该方法是支持读写锁的关键
                return baseInterrupt.lockAndRun(() -> {
                    try {
                        log.error("锁定资源: " + random);
                        Thread.sleep(1500);
                        baseInterrupt.interrupt();
                        return JobResult.completion("result");
                    } catch (InterruptedException e) {
                        return JobResult.exception(e);
                    }
                });
            } else if (random % 2 == 1) {
                return JobResult.failed(String.valueOf(random), String.valueOf(random));
            } else {
                return JobResult.completion(String.valueOf(random));
            }
        }, jobId);
    }

    /**
     * 任意一个线程的处理结果满足条件, 其他线程直接退出
     *
     * @throws Exception
     */
    @Test
    public void batchProcessIfAnyJobCompletion() throws Exception {
        JobBatchService<String> stringJobBatchService = new JobBatchService<>();
        String uuid = UUID.randomUUID().toString();
        List<Job<JobResult<String>>> threads = ForEachUtil.listJob(2000, () -> newTask(UUID.randomUUID().toString()));
        BatchInfo<JobResult<String>> defaultBatchJob = new DefaultBatchJob<>(UUID.randomUUID().toString(), uuid, threads);
        JobReport<JobStatus, JobResult<String>> jobReportContext = stringJobBatchService.batchProcess(
                defaultBatchJob
                , 3000
                , stringJobResult -> {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return stringJobResult.getJobStatus() == JobStatus.COMPLETION;
                }
                , false
        );
        print(jobReportContext);
    }

    /**
     * 处理所有任务
     *
     * @throws Exception
     */
    @Test
    public void batchProcess() throws Exception {
        JobBatchService<String> stringJobBatchService = new JobBatchService<>();
        String batchId = UUID.randomUUID().toString();
        List<Job<JobResult<String>>> jobs = ForEachUtil.listJob(2000, () -> {
            return new DefaultJob<>(
                    baseInterrupt -> JobResult.init()
                    , UUID.randomUUID().toString()
            );
        });
        BatchInfo<JobResult<String>> defaultBatchJob = new DefaultBatchJob<>(UUID.randomUUID().toString(), batchId, jobs);
        JobReport<JobStatus, JobResult<String>> jobReportContext = stringJobBatchService.batchProcess(
                defaultBatchJob
                , stringJobResult -> stringJobResult.getJobStatus() == JobStatus.COMPLETION
        );
        print(jobReportContext);
    }

    public void print(JobReport<JobStatus, JobResult<String>> jobReportContext) {
        log.info(jobReportContext);
      /*  log.warn("异常:" + jobContext.getExceptionInfo());
        log.warn("成功率:" + jobContext.getPercentage(JobStatus.COMPLETION));
        log.warn("错误率:" + jobContext.getPercentage(JobStatus.ERROR));
        log.warn("异常率:" + jobContext.getPercentage(JobStatus.EXCEPTION));
        log.warn("未处理比例:" + jobContext.getPercentage(JobStatus.SUBMIT));
        log.warn("被执行的任务总大小:" + jobContext.getJobSize());
        log.warn("特定目标资源总数:" + jobContext.getCompletionCount());
        Map<String, List<JobResult<String>>> exceptionInfo = jobContext.getExceptionInfo();
        Set<Map.Entry<String, List<JobResult<String>>>> entries = exceptionInfo.entrySet();
        entries.forEach(stringListEntry -> {
            log.warn(stringListEntry.getKey() + " : " + stringListEntry.getValue().size());
        });*/
    }

}
