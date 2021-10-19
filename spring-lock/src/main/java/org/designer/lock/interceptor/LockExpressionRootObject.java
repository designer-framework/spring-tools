package org.designer.lock.interceptor;

import lombok.Getter;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 18:25
 */
@Getter
public class LockExpressionRootObject {
    private final Method method;
    private final Object[] args;

    public LockExpressionRootObject(MethodInvocation methodInvocation) {
        method = methodInvocation.getMethod();
        args = methodInvocation.getArguments();
    }

}
