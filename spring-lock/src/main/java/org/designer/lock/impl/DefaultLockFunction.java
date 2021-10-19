package org.designer.lock.impl;

import org.designer.lock.LockFunction;
import org.designer.lock.interceptor.LockAttribute;

import java.util.concurrent.locks.Lock;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/11 10:40
 */
class DefaultLockFunction implements LockFunction {

    private final LockAttribute lockAttribute;

    private final Lock lock;

    /**
     * @param lock          锁
     * @param lockAttribute 锁参数
     */
    public DefaultLockFunction(
            Lock lock
            , LockAttribute lockAttribute
    ) {
        this.lock = lock;
        this.lockAttribute = lockAttribute;
    }

    @Override
    public boolean tryLock() throws InterruptedException {
        return lock.tryLock(lockAttribute.getTimeout(), lockAttribute.getTimeUnit());
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

}
