package org.designer.lock.interceptor;

import org.designer.lock.annotation.DefaultLockAnnotationParser;
import org.designer.lock.annotation.LockAnnotationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:20
 */
@Slf4j
public class DefaultLockAttributeSource implements LockAttributeSource {

    private static final LockAttribute NULL_LOCK_ATTRIBUTE = new DefaultLockAttribute() {
        @Override
        public String toString() {
            return "null";
        }
    };

    private final LockAnnotationParser annotationParsers;

    private final boolean publicMethodsOnly;

    private final Map<Object, LockAttribute> attributeCache = new ConcurrentHashMap<>(32);

    public DefaultLockAttributeSource() {
        annotationParsers = new DefaultLockAnnotationParser();
        publicMethodsOnly = true;
    }

    @Override
    public LockAttribute getLockAttribute(Method method, Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        } else {
            Object cacheKey = getCacheKey(method, targetClass);
            LockAttribute cached = attributeCache.get(cacheKey);
            if (cached != null) {
                return cached == NULL_LOCK_ATTRIBUTE ? null : cached;
            } else {
                LockAttribute lockAttr = computeLockAttribute(method, targetClass);
                if (lockAttr == null) {
                    attributeCache.put(cacheKey, NULL_LOCK_ATTRIBUTE);
                } else {
                    String methodIdentification = ClassUtils.getQualifiedMethodName(method, targetClass);
                    if (log.isTraceEnabled()) {
                        log.trace("Adding lock method '" + methodIdentification + "' lock attribute: " + lockAttr);
                    }
                    attributeCache.put(cacheKey, lockAttr);
                }
                return lockAttr;
            }
        }
    }


    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        return annotationParsers.isCandidateClass(targetClass);
    }

    @Nullable
    protected LockAttribute computeLockAttribute(Method method, @Nullable Class<?> targetClass) {
        //必须为public方法, 否则返回空
        if (publicMethodsOnly && !Modifier.isPublic(method.getModifiers())) {
            return null;
        } else {
            Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            //将方法上的锁注解转换成锁对象
            LockAttribute lockAttr = determineLockAttribute(specificMethod);
            if (lockAttr != null) {
                //如果表达式指定了condition为false则表示该方法不会被代理
                boolean condition = Boolean.FALSE.toString().equalsIgnoreCase(lockAttr.getCondition());
                if (condition) {
                    return null;
                    //该方法会被代理
                } else {
                    return lockAttr;
                }
            } else {
                //将类上的锁注解转换成锁参数对象
                lockAttr = determineLockAttribute(specificMethod.getDeclaringClass());
                if (lockAttr != null && ClassUtils.isUserLevelMethod(method)) {
                    return lockAttr;
                } else {
                    //桥接方法
                    if (specificMethod != method) {
                        lockAttr = determineLockAttribute(method);
                        if (lockAttr != null) {
                            return lockAttr;
                        }
                        //桥接类
                        lockAttr = determineLockAttribute(method.getDeclaringClass());
                        if (lockAttr != null && ClassUtils.isUserLevelMethod(method)) {
                            return lockAttr;
                        }
                    }
                    return null;
                }
            }
        }
    }

    @Nullable
    protected LockAttribute determineLockAttribute(AnnotatedElement element) {
        return annotationParsers.parseLockAnnotation(element);
    }

    protected Object getCacheKey(Method method, @Nullable Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

}
