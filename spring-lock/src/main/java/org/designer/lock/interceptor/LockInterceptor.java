package org.designer.lock.interceptor;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.namming.NameMapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 3:55
 * @see org.springframework.cache.interceptor.CacheInterceptor
 */
@Slf4j
public class LockInterceptor extends LockAspectjSupport implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton, MethodInterceptor, Ordered, Serializable {

    private static final long serialVersionUID = -6870926256867027381L;

    public LockInterceptor(
            AnnotationLockCreator annotationLockCreator
            , NameMapper nameMapper
    ) {
        super(annotationLockCreator, nameMapper);
    }

    public void setLockAttributeSource(LockAttributeSource lockAttributeSource) {
        this.lockAttributeSource = lockAttributeSource;
    }

    @Override
    @Nullable
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return executeService(invocation);
    }

}
