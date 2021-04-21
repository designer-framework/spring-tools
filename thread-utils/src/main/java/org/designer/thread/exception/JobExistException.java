package org.designer.thread.exception;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 21:21
 */
public class JobExistException extends BaseJobException {

    private static final long serialVersionUID = -4034004165660600405L;

    public JobExistException(String message, Throwable t) {
        super(message, t);
    }

    public JobExistException(String message) {
        super(message);
    }

}
