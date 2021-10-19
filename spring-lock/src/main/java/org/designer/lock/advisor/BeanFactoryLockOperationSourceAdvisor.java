package org.designer.lock.advisor;

import lombok.Getter;
import org.designer.lock.interceptor.LockAttributeSource;
import org.designer.lock.interceptor.LockOperationSourcePointcut;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @description: 候选者
 * @author: Designer
 * @date : 2021/9/19 3:27
 * @see org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor
 */
public class BeanFactoryLockOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private static final long serialVersionUID = -7423519967514082358L;

    @Getter
    @Nullable
    private LockAttributeSource lockAttributeSource;

    private final LockOperationSourcePointcut lockOperationSourcePointcut = new LockOperationSourcePointcut() {
        private static final long serialVersionUID = -4628469300655725639L;

        @Override
        protected LockAttributeSource getLockAttributeSource() {
            return lockAttributeSource;
        }

    };

    public void setLockAttributeSource(@Nullable LockAttributeSource lockAttributeSource) {
        this.lockAttributeSource = lockAttributeSource;
    }

    @Override
    public Pointcut getPointcut() {
        return new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> aClass) {
                return lockOperationSourcePointcut.matches(method, aClass);
            }
        };
    }

}
