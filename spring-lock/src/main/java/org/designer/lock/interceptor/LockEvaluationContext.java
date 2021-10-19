package org.designer.lock.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 18:25
 * @see org.springframework.cache.interceptor.CacheEvaluationContext
 */
class LockEvaluationContext extends MethodBasedEvaluationContext {

    LockEvaluationContext(Object rootObject, MethodInvocation invocation, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, invocation.getMethod(), invocation.getArguments(), parameterNameDiscoverer);
    }

}