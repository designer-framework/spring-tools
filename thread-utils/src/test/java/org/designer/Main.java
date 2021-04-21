package org.designer;

import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobResult;
import org.designer.thread.report.ReportForm;
import org.designer.thread.service.ThreadBatchService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:14
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ThreadBatchService<String> stringThreadBatchService = new ThreadBatchService<>();
        List<Job<String>> threads = ForEachUtil.listThread(20, newTask(LocalDateTime.now().toString()));
        ReportForm<String> stringReportForm = stringThreadBatchService.batchProcess(threads, "BATCH-" + UUID.randomUUID());
        System.out.println(stringReportForm);
    }

    public static Job<String> newTask(String jobName) {
        return new Job<>(() -> {
            Thread.sleep(new Random().nextInt(3) * 1000);
            long random = new Random().nextLong() % 2;
            JobResult<String> objectJobResult = new JobResult<>(jobName + random);
            if (random == 1) {
                objectJobResult.exception(new RuntimeException());
            } else if (random == 0) {
                objectJobResult.error(String.valueOf(random));
            } else {
                objectJobResult.setResult(String.valueOf(random));
            }
            return objectJobResult;
        }, jobName);
    }

}
