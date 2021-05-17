package org.designer.thread.exception;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 21:21
 */
public class JobRepeatInterruptException extends BaseJobException {

    private static final long serialVersionUID = -5995304578094083092L;

    public JobRepeatInterruptException(String message, Throwable t) {
        super(message, t);
    }

    public JobRepeatInterruptException(String message) {
        super(message);
    }

}
