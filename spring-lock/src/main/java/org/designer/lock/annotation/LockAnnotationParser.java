package org.designer.lock.annotation;

import org.designer.lock.interceptor.LockAttribute;
import org.springframework.lang.Nullable;

import java.lang.reflect.AnnotatedElement;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:21
 */
public interface LockAnnotationParser {

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }

    @Nullable
    LockAttribute parseLockAnnotation(AnnotatedElement var1);

}
