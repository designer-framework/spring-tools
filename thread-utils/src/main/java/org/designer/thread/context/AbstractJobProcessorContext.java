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
     *
     */
    protected final Interrupt interrupt;
    /**
     * 资源锁
     */
    protected final ReadWriteLock readWriteLock;
    /**
     * 判断为true时, 表示任务已完成, 其它新来的线程会直接中断
     */
    protected final Predicate<JobResult<T>> jobCompletionPredict;
    /**
     * 处理结果
     */
    protected final BlockingQueue<Future<JobResult<T>>> completionQueue;
    /**
     *
     */
    private final ThreadPoolExecutor threadPoolExecutor;
    /**
     * 线程池状态
     */
    private volatile int state = 0;

    /**
     * @param threadPoolExecutor
     * @param jobCompletionPredict
     */
    public AbstractJobProcessorContext(
            ThreadPoolExecutor threadPoolExecutor
            , Predicate<JobResult<T>> jobCompletionPredict
            , int queueCapacity
    ) {
        Assert.notNull(threadPoolExecutor, "线程池不能为空");
        this.jobCompletionPredict = jobCompletionPredict;
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
     * 在多个线程涌入的时候, 第一个线程在未完成CAS操作时, 第二个线程完成了CAS操作,
     * 此时第一个线程持有的的值不是最新的, 那么CAS操作会失败。
     * 如果不使用临时变量来获取当前state的最新值, 将会陷入死循环。
     * 通过 jstack 命令可以看到本方法一直在运行, 其它线程一直处于WAIT状态, 通过DEBUG反复测试, 较容易复现问题
     * 使用临时变量的目的: tmp及tmp+1代码块在入方法栈时并不是原子操作, 如果有一个更改state的操作执行在两代码块之间, 那么CAS操作就会失败
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
