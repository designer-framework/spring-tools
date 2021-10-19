package org.designer.lock.exception;

import org.springframework.expression.EvaluationException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/22 17:44
 */
public class LockKeyEvaluationException extends LockException {

    private static final long serialVersionUID = 632431773910807752L;

    public LockKeyEvaluationException(String message, EvaluationException cause) {
        super(message, cause);
    }

}
