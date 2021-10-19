package org.designer.lock.interceptor;

import org.designer.lock.annotation.Key;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:17
 */
@Getter
@Setter
public class DefaultLockAttribute implements LockAttribute {

    /**
     * 缓存key
     */
    private Key[] key;

    /**
     * 获取锁的超时时间, 默认为-1. 无法获取到锁会一直等待, 直到获取到锁
     */
    private int timeout;

    /**
     * 获取锁的超时时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 表达式返回true才会执行加锁流程
     */
    private String condition;

    private String prefix;

    @Override
    public Key[] getKey() {
        return key;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

}
