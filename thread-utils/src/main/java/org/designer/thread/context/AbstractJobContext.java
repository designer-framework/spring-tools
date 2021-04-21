package org.designer.thread.context;

import org.designer.thread.MyExecutorCompletionService;
import org.designer.thread.MyThreadPoolExecutor;
import org.designer.thread.callable.JobCallable;
import org.designer.thread.entity.Job;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.exception.JobExistException;
import org.designer.thread.property.CompletionServiceProperty;
import org.designer.thread.report.job.JobReportContext;
import org.designer.thread.report.job.JobReportContextImpl;
import org.designer.thread.utils.MathUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 13:50
 */
public abstract class AbstractJobContext<T> implements AutoCloseable, JobContext<JobStatus, T> {
    /**
     *
     */
    protected final MyThreadPoolExecutor threadPoolExecutor = MyThreadPoolExecutor.getInstance("JOB - " + new Date(), 3000);

    /**
     *
     */
    protected final MyExecutorCompletionService<JobResult<T>> myExecutorCompletionService = MyExecutorCompletionService
            .getInstance(
                    CompletionServiceProperty.<JobResult<T>>builder().threadPoolExecutor(threadPoolExecutor)
                            .queue(new ArrayBlockingQueue<>(3000)).build()
            );
    /**
     *
     */
    protected final Map<String, JobResult<T>> futureMap = new ConcurrentHashMap<>();
    /**
     *
     */
    protected final JobReportContext<JobStatus, JobResult<T>> jobReportContext = new JobReportContextImpl<>();

    private static <T> JobCallable<T> copyJobToTask(Job<T> job) {
        return JobCallable.<T>builder()
                .batchId(job.getBatchId())
                .jobId(job.getJobId())
                .task(job.getTask())
                .createTime(job.getCreateTime())
                .build();
    }

    //private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String getPercentage(JobStatus jobStatus) {
        return MathUtils.computePercentage(jobReportContext.getSizeByKey(jobStatus), jobReportContext.size());
    }

    public int getJobQueueSize() {
        return futureMap.size();
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
                    jobReportContext.submit(result.get());
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
    public void submitJob(Job<T> job) {
        if (futureMap.containsKey(job.getJobId())) {
            JobResult<T> tJobResult = new JobResult<>(job.getJobId());
            tJobResult.exception(new JobExistException(job.getJobId() + ", 任务名字重复"));
            jobReportContext.submit(tJobResult);
        } else {
            //counter.incrementAndGet();
            Future<JobResult<T>> jobResultFuture = myExecutorCompletionService.submit(copyJobToTask(job));
            futureMap.put(job.getJobId(), new JobResult<>(jobResultFuture, job.getJobId()));
        }
    }

    @Override
    public void close() throws Exception {
        threadPoolExecutor.shutdown();
    }

}
