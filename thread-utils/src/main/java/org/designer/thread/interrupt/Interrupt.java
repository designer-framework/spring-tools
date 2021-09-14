package org.designer.thread.interrupt;

/**
 * @description: FIXME 将Completion职责分离
 * @author: Designer
 * @date : 2021/4/20 19:21
 */
public interface Interrupt extends BaseInterrupt {

    /**
     * 将线程挂起
     *
     * @param interrupt
     */
    void setInterrupt(Boolean interrupt);

    /**
     * 类似Redis脚本, 如果当前没有被挂起, 则将当前线程挂起, 若当前线程被挂起则什么都不做并返回
     */
    @Override
    default boolean interrupt() {
        if (!getInterrupt()) {
            setInterrupt(true);
            return true;
        }
        return false;
    }

    /**
     * 当前任务是否已经完成
     *
     * @return
     */
    public boolean isCompletion();


}