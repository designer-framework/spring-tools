package org.designer.lock.exception;

/**
 * @description: 加锁失败
 * @author: Designer
 * @date : 2021/9/20 20:05
 */
public class TryLockException extends LockException {

    private static final long serialVersionUID = 2567250801168735446L;

    public TryLockException(String message) {
        super(message);
    }

}
