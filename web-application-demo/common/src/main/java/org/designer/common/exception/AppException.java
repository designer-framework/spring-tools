package org.designer.common.exception;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.exception
 * @Author: Designer
 * @CreateTime: 2021-04-24 19
 * @Description:
 */

public abstract class AppException extends RuntimeException {

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    protected AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
