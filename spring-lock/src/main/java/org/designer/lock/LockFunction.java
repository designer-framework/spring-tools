package org.designer.lock;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 22:47
 */
public interface LockFunction {

    /**
     * @return 是否成功获取到锁
     */
    boolean tryLock() throws InterruptedException;

    /**
     * 解锁
     *
     * @return 主动关闭锁
     */
    void unlock();

}
