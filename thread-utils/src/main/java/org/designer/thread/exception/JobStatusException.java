package org.designer.thread.exception;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 21:21
 */
public class JobStatusException extends BaseJobException {

    private static final long serialVersionUID = -4034004165660600405L;

    public JobStatusException(String message, Throwable t) {
        super(message, t);
    }

    public JobStatusException(String message) {
        super(message);
    }

}
