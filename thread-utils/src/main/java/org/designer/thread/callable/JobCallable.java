package org.designer.thread.callable;

import org.designer.thread.interrupt.BaseInterrupt;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/28 17:49
 */
public interface JobCallable<V> {

    /**
     * @param
     * @return
     * @throws Exception
     */
    V call(BaseInterrupt baseInterrupt) throws Exception;

}
