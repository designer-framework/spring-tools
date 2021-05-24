package org.designer.thread.interrupt;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.exception.JobRepeatInterruptException;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/28 18:33
 */
@Log4j2
public abstract class AbstractInterruptImpl implements Interrupt {

    private final ReadWriteLock readWriteLock;

    private volatile Boolean isInterrupt = false;

    public AbstractInterruptImpl(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public Boolean getInterrupt() {
        return isInterrupt;
    }

    @Override
    public void setInterrupt(Boolean interrupt) {
        isInterrupt = interrupt;
    }

    @Override
    public synchronized boolean interrupt() throws JobRepeatInterruptException {
        return Interrupt.super.interrupt();
    }

    /**
     * 避免死锁, 先解读锁, 再加写锁. 因为在执行该方法之前调用了读锁.
     * 为遵循开闭原则及易用性, 所以不暴露读写锁给用户
     *
     * @param runnable
     * @throws Exception
     */
    @Override
    public void lockAndRun(CallRunnable runnable) throws Exception {
        readWriteLock.readLock().unlock();
        readWriteLock.writeLock().lock();
        try {
            log.debug("锁定资源");
            runnable.run();
            log.debug("资源获取完成");
        } finally {
            readWriteLock.writeLock().unlock();
            readWriteLock.readLock().lock();
        }
    }


}
