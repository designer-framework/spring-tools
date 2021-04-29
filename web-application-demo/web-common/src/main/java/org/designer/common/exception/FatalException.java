package org.designer.common.exception;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.exception
 * @Author: Designer
 * @CreateTime: 2021-04-24 19
 * @Description:
 */

public class FatalException extends AppException {

    private static final long serialVersionUID = 6895625452527783032L;

    public FatalException(String message) {
        super(message);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }
}
