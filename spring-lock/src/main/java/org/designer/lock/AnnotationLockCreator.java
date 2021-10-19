package org.designer.lock;

import org.designer.lock.interceptor.LockAttribute;

/**
 * @description: 传入锁以及锁相关的参数, 返回一个只包含加锁解锁功能的Lock
 * @author: Designer
 * @date : 2021/9/19 22:47
 */
public interface AnnotationLockCreator {

    /**
     * 锁定
     *
     * @param key
     * @return 返回一个可被调用者主动关闭的锁
     */
    LockFunction getLock(Object key, LockAttribute lockAttribute);

}
