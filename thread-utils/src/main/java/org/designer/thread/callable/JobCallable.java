package org.designer.thread.callable;

import org.designer.thread.context.JobProcessorContextImpl;
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
     * @see JobProcessorContextImpl#copyJobToTask
     * 该接口是对外屏蔽内部细节的关键, 它将线程池内部的锁不直接暴露给开发者, 而是将锁进一步封装成易用的BaseInterrupt接口
     */
    V call(BaseInterrupt baseInterrupt) throws Exception;

}
