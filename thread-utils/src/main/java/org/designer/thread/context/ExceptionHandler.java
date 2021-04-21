package org.designer.thread.context;

import org.designer.thread.entity.JobResult;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 0:08
 */
public class ExceptionHandler<T> {

    public JobResult<T> handler(JobResult<T> tJobResult, Exception e) throws IllegalStateException {
        if (tJobResult == null) {
            throw new IllegalStateException("callable 异常");
        }
        tJobResult.exception(e);
        return tJobResult;
    }

}
