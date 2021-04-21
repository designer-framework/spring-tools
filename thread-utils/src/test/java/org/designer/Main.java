package org.designer;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.context.JobContext;
import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.service.JobBatchService;

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

    public static void main(String[] args) throws Exception {
        JobBatchService<String> stringJobBatchService = new JobBatchService<>();
        String uuid = UUID.randomUUID().toString();
        List<Job<String>> threads = ForEachUtil.listThread(200, () -> newTask(UUID.randomUUID().toString(), uuid));
        JobContext<JobStatus, String> jobStatusStringJobContext = stringJobBatchService.batchProcess(threads, "BATCH-" + UUID.randomUUID());
        log.debug("成功率:" + jobStatusStringJobContext.getPercentage(JobStatus.COMPLETION));
    }

    public static Job<String> newTask(String jobId, String batchId) {
        return new Job<>(() -> {
            Thread.sleep(new Random().nextInt(3) * 1000);
            int random = new Random().nextInt(100);
            JobResult<String> objectJobResult = new JobResult<>(jobId + UUID.randomUUID());
            if (random % 10 == 0) {
                objectJobResult.exception(new RuntimeException());
            } else if (random % 2 == 1) {
                objectJobResult.error(String.valueOf(random));
            } else {
                objectJobResult.setResult(String.valueOf(random));
            }
            return objectJobResult;
        }, jobId, batchId);
    }

}
