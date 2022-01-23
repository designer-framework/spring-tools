package org.designer.thread.context;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.interrupt.Interrupt;
import org.designer.thread.interrupt.InterruptImpl;
import org.designer.thread.job.JobResult;
import org.designer.thread.utils.UnsafeUtil;
import org.designer.thread.utils.builder.ExecutorCompletionServiceBuilder;
import org.springframework.util.Assert;
import sun.misc.Unsafe;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 13:50
 */
@Log4j2
public abstract class AbstractJobProcessorContext<T> extends Thread implements AutoCloseable, JobProcessorContext<JobResult<T>> {

    private final static Unsafe UNSAFE = UnsafeUtil.getUnsafe();

    private static final long stateOffset;

    static {
        try {
            stateOffset = UNSAFE.objectFieldOffset(AbstractJobProcessorContext.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    protected final ExecutorCompletionService<JobResult<T>> executorCompletionService;
    /**
     * 当完成任务可以调用interrupt方法, 终止后续任务
     */
    protected final Interrupt interrupt;
    /**
     * 资源独占锁
     */
    protected final ReadWriteLock readWriteLock;
    /**
     * 判断为true时, 表示任务已完成, 其它新来的线程会直接中断
     */
    protected final Predicate<JobResult<T>> jobInterruptPredict;
    /**
     * 存放线程处理结果
     */
    protected final BlockingQueue<Future<JobResult<T>>> completionQueue;
    /**
     *
     */
    private final ThreadPoolExecutor threadPoolExecutor;
    /**
     * 线程池状态, 大于0则表示任务已完成
     */
    private volatile int state = 0;

    /**
     * @param threadPoolExecutor
     * @param jobInterruptPredict
     */
    public AbstractJobProcessorContext(
            ThreadPoolExecutor threadPoolExecutor
            , Predicate<JobResult<T>> jobInterruptPredict
            , int queueCapacity
    ) {
        Assert.notNull(threadPoolExecutor, "线程池不能为空");
        this.jobInterruptPredict = jobInterruptPredict;
        this.threadPoolExecutor = threadPoolExecutor;
        completionQueue = new ArrayBlockingQueue<>(queueCapacity);
        executorCompletionService = new ExecutorCompletionServiceBuilder<JobResult<T>>()
                .build(threadPoolExecutor, completionQueue, queueCapacity);
        readWriteLock = new ReentrantReadWriteLock(false);
        interrupt = new InterruptImpl(readWriteLock, this::isCompletion);

    }

    protected boolean isCompletion() {
        return state > 0;
    }

    /**
     * 在多个线程涌入的时候, 第一个线程在未完成CAS操作时, 第二个线程比第一个线程先完成CAS操作,
     * 那么第一个CAS操作会失败, 并会重试
     */
    protected void completion() {
        int tmp = state;
        //CAS自旋
        while (!UNSAFE.compareAndSwapInt(this, stateOffset, tmp, tmp + 1)) {
            tmp = state;
        }
    }

    @Override
    public int getCompletionCount() {
        return state;
    }

    @Override
    public void close() throws Exception {
        threadPoolExecutor.shutdown();
    }

}
