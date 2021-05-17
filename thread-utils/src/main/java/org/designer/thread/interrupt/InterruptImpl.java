package org.designer.thread.interrupt;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/28 18:33
 */
public class InterruptImpl extends AbstractInterruptImpl {

    private final Supplier<Boolean> state;

    public InterruptImpl(ReadWriteLock readWriteLock, Supplier<Boolean> state) {
        super(readWriteLock);
        this.state = state;
    }

    @Override
    public boolean isCompletion() {
        return state.get();
    }

}
