package org.designer.lock.interceptor;

import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:11
 */
public interface LockAttributeSource {

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }

    /**
     * 将方法或类上的注解信息转换成对象
     *
     * @param var1
     * @param var2
     * @return
     */
    @Nullable
    LockAttribute getLockAttribute(Method var1, @Nullable Class<?> var2);

}
