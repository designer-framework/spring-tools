package org.designer.lock.interceptor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 3:28
 */
public abstract class LockOperationSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

    private static final long serialVersionUID = 81817606031241381L;

    protected abstract LockAttributeSource getLockAttributeSource();

    @Override
    public ClassFilter getClassFilter() {
        return new LockOperationSourceClassFilter();
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        LockAttributeSource las = getLockAttributeSource();
        return las != null && las.getLockAttribute(method, targetClass) != null;
    }

    private class LockOperationSourceClassFilter implements ClassFilter {

        @Override
        public boolean matches(Class<?> clazz) {
            LockAttributeSource las = getLockAttributeSource();
            return las == null || las.isCandidateClass(clazz);
        }

    }

}
