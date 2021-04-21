package org.designer.thread;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:21
 */
public interface Interrupt extends BaseInterrupt {

    void setInterrupt(Boolean interrupt);

    @Override
    default void interrupt() {
        if (!getInterrupt()) {
            setInterrupt(true);
        }
    }

}