package org.designer.thread.context;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.MyExecutorCompletionService;
import org.designer.thread.MyThreadPoolExecutor;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.interrupt.BaseInterrupt;
import org.designer.thread.interrupt.InterruptImpl;
import org.designer.thread.property.CompletionServiceProperty;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 13:50
 */
@Log4j2
public abstract class AbstractJobContext<T> implements JobContext<JobStatus, T> {

    /**
     *
     */
    protected final MyThreadPoolExecutor threadPoolExecutor;

    /**
     *
     */
    protected final MyExecutorCompletionService<JobResult<T>> myExecutorCompletionService;

    protected final BaseInterrupt baseInterrupt;

    protected final ReadWriteLock readWriteLock;

    /**
     * 判断为true时, 表示任务已完成
     */
    protected final Predicate<JobResult<T>> processorCompletionPredict;

    public AbstractJobContext(int queueSize) {
        this(queueSize, true, null);
    }

    public AbstractJobContext(int queueSize, Predicate<JobResult<T>> processorCompletionPredict) {
        this(queueSize, true, processorCompletionPredict);
    }

    public AbstractJobContext(int queueSize, boolean fair, Predicate<JobResult<T>> processorCompletionPredict) {
        this.processorCompletionPredict = processorCompletionPredict;
        threadPoolExecutor = MyThreadPoolExecutor.getInstance("JOB - " + new Date(), queueSize);
        myExecutorCompletionService = MyExecutorCompletionService.getInstance(
                CompletionServiceProperty
                        .<JobResult<T>>builder()
                        .threadPoolExecutor(threadPoolExecutor)
                        .queue(new ArrayBlockingQueue<>(queueSize)).build()
        );
        readWriteLock = new ReentrantReadWriteLock(fair);
        baseInterrupt = new InterruptImpl(readWriteLock);
    }

    abstract void submitReport(JobResult<T> tJobResult);

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
                    submitReport(result.get());
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
