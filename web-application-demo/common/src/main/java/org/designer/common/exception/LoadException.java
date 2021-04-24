package org.designer.common.exception;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.exception
 * @Author: Designer
 * @CreateTime: 2021-04-24 19
 * @Description:
 */

public class LoadException extends AppException {

    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
