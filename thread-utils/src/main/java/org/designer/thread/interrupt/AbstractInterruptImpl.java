package org.designer.thread.interrupt;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/28 18:33
 */
@Log4j2
public class AbstractInterruptImpl implements Interrupt {

    private final ReadWriteLock writeLock;

    private volatile Boolean isInterrupt = false;

    public AbstractInterruptImpl(ReadWriteLock writeLock) {
        this.writeLock = writeLock;
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
    public synchronized void interrupt() {
        Interrupt.super.interrupt();
    }

    /**
     * 避免死锁, 先解读锁, 再加写锁
     * 为遵循开闭原则及易用性, 所以不暴露读写锁给用户
     *
     * @param runnable
     * @throws Exception
     */
    @Override
    public void lockAndRun(CallRunnable runnable) throws Exception {
        writeLock.readLock().unlock();
        writeLock.writeLock().lock();
        try {
            log.debug("锁定资源");
            runnable.run();
            log.debug("资源获取完成");
        } finally {
            writeLock.writeLock().unlock();
            writeLock.readLock().lock();
        }
    }

}
