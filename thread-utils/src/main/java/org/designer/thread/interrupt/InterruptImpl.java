package org.designer.thread.interrupt;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/28 18:33
 */
public class InterruptImpl extends AbstractInterruptImpl {

    public InterruptImpl(ReadWriteLock readWriteLock) {
        super(readWriteLock);
    }

}
