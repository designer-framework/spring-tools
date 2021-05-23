package org.designer.handler;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/23 23:20
 */
public interface ExceptionHandler<J, T> {

    /**
     * 任务执行出现异常时, 将异常包装成任务结果, 避免了代码抛出的异常导致程序直接中断
     *
     * @param e
     * @return
     */
    T handler(Exception e, J requestInfo);

}
