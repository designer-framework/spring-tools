package org.designer.thread.exception;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 21:20
 */
public abstract class BaseJobException extends RuntimeException {
    private static final long serialVersionUID = 7145355895541893954L;

    public BaseJobException() {
        super();
    }

    public BaseJobException(String message) {
        super(message);
    }

    public BaseJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseJobException(Throwable cause) {
        super(cause);
    }

    protected BaseJobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
