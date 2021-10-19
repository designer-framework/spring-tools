package org.designer.lock.interceptor;

import org.designer.lock.annotation.Key;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:12
 */
public interface LockDefinition {

    /**
     * 待加锁的key, 此处获取的是EL表达式
     *
     * @return
     */
    Key[] getKey();

    /**
     * 获取锁的超时时间, -1会一直等待锁
     *
     * @return
     */
    int getTimeout();

    /**
     * 获取锁的等待时间
     *
     * @return
     */
    TimeUnit getTimeUnit();

    /**
     * 表达式返回true才会执行加锁流程
     */
    String getCondition();

}
