package org.designer.thread.interrupt;

/**
 * @description: 线程状态
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
     * 当发现目标资源时, 对资源进行锁定, 其他新来的线程都将等待. 这样可以让更多的系统资源来处理目标资源.
     * 避免其它资源并行处理, 导致获取目标资源的线程被拖累
     *
     * @param runnable
     */
    void lockAndRun(CallRunnable runnable) throws Exception;


}
