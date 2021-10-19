package org.designer.lock.exception;

/**
 * @description: 注解锁异常
 * @author: Designer
 * @date : 2021/9/20 20:05
 */
public abstract class LockException extends RuntimeException {

    private static final long serialVersionUID = 2567250801168735446L;

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

}
