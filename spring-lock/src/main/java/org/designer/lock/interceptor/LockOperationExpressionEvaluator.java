package org.designer.lock.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 18:26
 * @see org.springframework.cache.interceptor.CacheOperationExpressionEvaluator
 * @see org.springframework.expression.spel.standard.TokenKind
 */
public class LockOperationExpressionEvaluator extends LockedExpressionEvaluator {

    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(32);

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(32);

    private final Map<ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<>(32);

    protected LockOperationExpressionEvaluator() {
        super(new SpelExpressionParser());
    }

    public EvaluationContext createEvaluationContext(MethodInvocation methodInvocation, @Nullable BeanFactory beanFactory) {
        LockExpressionRootObject rootObject = new LockExpressionRootObject(methodInvocation);
        LockEvaluationContext evaluationContext = new LockEvaluationContext(rootObject, methodInvocation, getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    @Nullable
    public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) throws EvaluationException {
        return getExpression(keyCache, methodKey, keyExpression).getValue(evalContext);
    }

    public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(conditionCache, methodKey, conditionExpression).getValue(
                evalContext, Boolean.class)));
    }

    private boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(unlessCache, methodKey, unlessExpression).getValue(
                evalContext, Boolean.class)));
    }

}