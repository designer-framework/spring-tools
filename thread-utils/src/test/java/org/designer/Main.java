package org.designer;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.context.JobContext;
import org.designer.thread.entity.DefaultJobInfo;
import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.service.JobBatchService;
import org.junit.Test;

import java.util.*;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:14
 */
@Log4j2
public class Main {

    public static Job<String> newTask(String jobId, String batchId) {
        return new DefaultJobInfo<>((baseInterrupt) -> {
            JobResult<String> objectJobResult = new JobResult<>(batchId, jobId);
            int random = new Random().nextInt(1000);
            Thread.sleep(new Random().nextInt(2) * 1000);
            if (random % 101 == 0) {
                //该方法是支持读写锁的关键
                baseInterrupt.lockAndRun(() -> {
                    try {
                        log.error("锁定资源: " + random);
                        Thread.sleep(1500);
                        baseInterrupt.interrupt();
                        objectJobResult.exception(new RuntimeException());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else if (random % 2 == 1) {
                objectJobResult.error(String.valueOf(random));
            } else {
                objectJobResult.setResult(String.valueOf(random));
            }
            return objectJobResult;
        }, jobId, batchId);
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
        List<Job<String>> threads = ForEachUtil.listJob(2000, () -> newTask(UUID.randomUUID().toString(), uuid));
        JobContext<JobStatus, String> jobContext = stringJobBatchService.batchProcess(
                threads
                , "BATCH-" + UUID.randomUUID()
                , 3000
                , stringJobResult -> stringJobResult.getJobStatus() == JobStatus.COMPLETION
        );
        print(jobContext);
    }

    /**
     * 处理所有任务
     *
     * @throws Exception
     */
    @Test
    public void batchProcess() throws Exception {
        JobBatchService<String> stringJobBatchService = new JobBatchService<>();
        String uuid = UUID.randomUUID().toString();
        List<Job<String>> jobs = ForEachUtil.listJob(2000, () -> newTask(UUID.randomUUID().toString(), uuid));
        JobContext<JobStatus, String> jobContext = stringJobBatchService.batchProcess(jobs, "BATCH-" + UUID.randomUUID());
        print(jobContext);
    }

    public void print(JobContext<JobStatus, String> jobContext) {
        log.warn("异常:" + jobContext.getExceptionInfo());
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
        });
    }

}
