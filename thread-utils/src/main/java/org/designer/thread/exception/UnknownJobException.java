package org.designer.thread.exception;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/23 21:21
 */
public class UnknownJobException extends BaseJobException {

    private static final long serialVersionUID = 4631108372537844691L;

    public UnknownJobException(String jobName, Throwable t) {
        super(jobName, t);
    }

}
