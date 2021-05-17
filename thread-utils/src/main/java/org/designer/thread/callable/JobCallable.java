package org.designer.thread.callable;

import org.designer.thread.interrupt.BaseInterrupt;

/**
 * @description: 封装Callable
 * @author: Designer
 * @date : 2021/4/28 17:49
 */
@FunctionalInterface
public interface JobCallable<V> {

    /**
     * @param baseInterrupt 通过baseInterrupt控制资源
     * @return
     * @throws Exception
     */
    V call(BaseInterrupt baseInterrupt) throws Exception;

}
