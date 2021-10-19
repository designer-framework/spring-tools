package org.designer.lock.interceptor;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.LockFunction;
import org.designer.lock.annotation.Key;
import org.designer.lock.exception.CacheKeyNotFoundException;
import org.designer.lock.exception.LockKeyEvaluationException;
import org.designer.lock.exception.TryLockException;
import org.designer.lock.namming.NameMapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/20 19:25
 */
@Slf4j
public abstract class LockAspectjSupport implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton, MethodInterceptor, Ordered, Serializable {

    private static final long serialVersionUID = 5642959623931012869L;
    /**
     * key下标从0开始, 和数组下标保持一致
     */
    private static final String CACHE_KEY_NOT_FOUNT = "调用方法[%s]时key为空! key的数组下标[%s].";
    protected final transient AnnotationLockCreator annotationLockCreator;
    protected final transient NameMapper nameMapper;
    private final transient LockOperationExpressionEvaluator evaluator = new LockOperationExpressionEvaluator();
    protected transient LockAttributeSource lockAttributeSource;
    private transient BeanFactory beanFactory;

    /**
     * @param annotationLockCreator
     */
    protected LockAspectjSupport(AnnotationLockCreator annotationLockCreator, NameMapper nameMapper) {
        this.annotationLockCreator = annotationLockCreator;
        this.nameMapper = nameMapper;
    }

    /**
     * 执行业务逻辑
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    protected Object executeService(MethodInvocation invocation) throws Throwable {
        //提取类上或方法上的锁参数
        LockAttribute lockAttribute = getLockAttribute(invocation);
        //如果表达式不为空且表达式计算出来的值为false, 则不走加锁流程
        if (!StringUtils.isEmpty(lockAttribute.getCondition()) && !getCondition(invocation, lockAttribute)) {
            return invocation.proceed();
        } else {
            //计算锁的key值
            try {
                Object[] lockKeys = getLockKey(invocation, lockAttribute);
                if (lockKeys.length == 0) {
                    return invocation.proceed();
                } else {
                    return lockAndInvoke(lockAttribute, lockKeys, invocation);
                }
            } catch (EvaluationException e) {
                throw new LockKeyEvaluationException("资源加锁异常", e);
            }
        }
    }

    private Object lockAndInvoke(LockAttribute lockAttribute, Object[] lockKeys, MethodInvocation invocation) throws Throwable {
        LockChain lockChain = new LockChain(lockAttribute, lockKeys, invocation);
        return lockChain.lockAndInvoke();
    }

    private Object[] getLockKey(MethodInvocation invocation, LockAttribute lockAttribute) throws EvaluationException {
        //取出key表达式
        Key[] keys = lockAttribute.getKey();
        //不使用 lambda 便于代码阅读
        return Arrays.stream(keys).map(key -> {
                    //计算key值
                    Object lockKey = evaluator.key(
                            key.name()
                            , createExpressionCacheKey(invocation)
                            , createEvaluationContext(invocation)
                    );
                    Assert.notNull(lockKey, () -> "锁key不能为空!");
                    if (StringUtils.hasText(key.prefix())) {
                        return nameMapper.create(key.prefix(), lockKey);
                    } else {
                        return nameMapper.create(lockAttribute.getPrefix(), lockKey);
                    }
                }
        ).toArray();
    }

    private boolean getCondition(MethodInvocation invocation, LockAttribute lockAttribute) {
        //
        return evaluator.condition(
                lockAttribute.getCondition()
                , createExpressionCacheKey(invocation)
                , createEvaluationContext(invocation)
        );
    }

    private AnnotatedElementKey createExpressionCacheKey(MethodInvocation invocation) {
        return new AnnotatedElementKey(
                invocation.getMethod()
                , invocation.getThis().getClass()
        );
    }

    /**
     * 创建EvaluationContext
     *
     * @param invocation
     * @return
     */
    private EvaluationContext createEvaluationContext(MethodInvocation invocation) {
        return evaluator.createEvaluationContext(invocation, beanFactory);
    }

    private LockAttribute getLockAttribute(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        LockAttribute lockAttribute = lockAttributeSource.getLockAttribute(method, method.getDeclaringClass());
        Assert.notNull(lockAttribute, "LockAttribute 参数不能为空");
        return lockAttribute;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void afterSingletonsInstantiated() {
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private class LockChain {

        private final LockAttribute lockAttribute;
        private final Object[] lockKeys;
        private final MethodInvocation invocation;
        private int lockIndex;

        /**
         * @param lockAttribute 注解参数
         * @param lockKeys      从参数中解析出来的锁的key
         * @param invocation    invocation
         */
        public LockChain(LockAttribute lockAttribute, Object[] lockKeys, MethodInvocation invocation) {
            this.lockAttribute = lockAttribute;
            this.lockKeys = lockKeys;
            this.invocation = invocation;
        }

        private Object lockAndInvoke() throws Throwable {
            Object lockKey = lockKeys[lockIndex];
            if (lockKey == null) {
                throw new CacheKeyNotFoundException(String.format(CACHE_KEY_NOT_FOUNT, invocation.getMethod().toGenericString(), lockIndex));
            }
            if (log.isTraceEnabled()) {
                log.trace("尝试获取锁, key: [{}]", lockKey);
            }
            LockFunction lockFunction = null;
            boolean hasLock = false;
            try {
                //获取锁
                lockFunction = annotationLockCreator.getLock(lockKey, lockAttribute);
                if (lockFunction.tryLock()) {
                    hasLock = true;
                    if (log.isTraceEnabled()) {
                        log.trace("锁获取成功, key: [{}]", lockKey);
                    }
                } else {
                    throw new TryLockException("尝试获取锁失败, key: " + lockKey);
                }
                //锁都已经上好了, 则执行业务
                if (lockIndex >= lockKeys.length - 1) {
                    return invocation.proceed();
                } else {
                    lockIndex++;
                    return lockAndInvoke();
                }
            } finally {
                try {
                    if (lockFunction == null) {
                        log.warn("锁获取失败, key: [{}]", lockKey);
                    } else {
                        if (hasLock) {
                            //释放锁
                            lockFunction.unlock();
                            if (log.isTraceEnabled()) {
                                log.trace("锁释放成功, key: [{}]", lockKey);
                            }
                        }
                    }
                    //释放失败
                } catch (Exception e) {
                    log.error("锁释放异常, key: " + lockKey, e);
                }
            }
        }
    }


}
