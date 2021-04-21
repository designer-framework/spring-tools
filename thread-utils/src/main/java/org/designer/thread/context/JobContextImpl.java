package org.designer.thread.context;

import org.designer.thread.MyExecutorCompletionService;
import org.designer.thread.MyThreadPoolExecutor;
import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobResult;
import org.designer.thread.property.CompletionServiceProperty;
import org.designer.thread.report.ReportContext;
import org.designer.thread.report.ReportContextImpl;
import org.designer.thread.report.ReportForm;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:23
 */
public class JobContextImpl<T> implements AutoCloseable, JobContext<T> {

    private final MyThreadPoolExecutor threadPoolExecutor = MyThreadPoolExecutor.getInstance("JOB - " + new Date(), 3000);

    private final MyExecutorCompletionService<JobResult<T>> myExecutorCompletionService = MyExecutorCompletionService
            .getInstance(
                    CompletionServiceProperty.<JobResult<T>>builder().threadPoolExecutor(threadPoolExecutor)
                            .queue(new ArrayBlockingQueue<>(3000)).build()
            );

    private final Map<String, JobResult<T>> futureMap = new ConcurrentHashMap<>();

    private final ReportContext<T> reportContext = new ReportContextImpl<>();

    //private final AtomicInteger counter = new AtomicInteger(0);

    public int getJobQueueSize() {
        return futureMap.size();
    }

    @Override
    public void submitJob(Job<T> jobTask) {
        if (futureMap.containsKey(jobTask.getJobName())) {
            JobResult<T> tJobResult = new JobResult<>(jobTask.getJobName());
            tJobResult.exception(new RuntimeException(jobTask.getJobName() + ", 任务名字重复"));
            reportContext.submit(tJobResult);
        } else {
            //counter.incrementAndGet();
            Future<JobResult<T>> jobResultFuture = myExecutorCompletionService.submit(jobTask);
            futureMap.put(jobTask.getJobName(), new JobResult<>(jobResultFuture, jobTask.getJobName()));
        }
    }

    @Override
    public ReportForm<T> getReportForm() {
        return reportContext;
    }

    @Override
    public boolean pollJob(int count) throws InterruptedException, ExecutionException {
        if (count <= 0) {
            return true;
        }
        int i = 0;
        while (i != count) {
            Future<JobResult<T>> result;
            while ((result = myExecutorCompletionService.poll()) != null) {
                try {
                    JobResult<T> tJobResult = result.get();
                    reportContext.submit(tJobResult);
                } catch (ExecutionException e) {
                    throw e;
                } finally {
                    i++;
                }
            }
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        threadPoolExecutor.shutdown();
    }

}
