package org.designer.lock.impl;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.LockFunction;
import org.designer.lock.interceptor.LockAttribute;
import org.redisson.api.RedissonClient;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/11 13:51
 */
public class RedissonAnnotationLockCreator implements AnnotationLockCreator {

    private final RedissonClient redissonClient;

    public RedissonAnnotationLockCreator(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public LockFunction getLock(Object key, LockAttribute lockAttribute) {
        return new DefaultLockFunction(redissonClient.getLock(String.valueOf(key)), lockAttribute);
    }

}
