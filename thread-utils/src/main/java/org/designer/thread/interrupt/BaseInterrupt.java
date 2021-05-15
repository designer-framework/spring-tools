package org.designer.thread.interrupt;

/**
 * @description: 线程池挂起状态控制
 * @author: Designer
 * @date : 2021/4/20 19:11
 */
public interface BaseInterrupt {

    /**
     * 获取当前线程状态, 是否被挂起
     *
     * @return
     */
    Boolean getInterrupt();

    /**
     * 如果当前线程状态未挂起, 则调用挂起
     */
    void interrupt();

    /**
     * 当发现目标资源时, 对线程池进行锁定, 其他新来的线程都将等待. 这样可以让更多的CPU时间片用来获取目标资源.
     * 以此来避免其它查找资源的新线程并行处理, 导致获取目标资源的线程只有少量CPU资源可用。
     * 需要注意的是, 当前尝试获取资源的线程暂时不支持中断。后期考虑完善
     *
     * @param runnable
     */
    void lockAndRun(CallRunnable runnable) throws Exception;


}
