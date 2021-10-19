package org.designer.lock.annotation;

import org.designer.lock.interceptor.DefaultLockAttribute;
import org.designer.lock.interceptor.LockAttribute;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: 对类和方法进行判断, 是否符合被代理的要求。 如果符合要求就将注解信息转换成对象
 * @author: Designer
 * @date : 2021/9/19 21:22
 */
public class DefaultLockAnnotationParser implements LockAnnotationParser {

    private static final Set<Class<? extends Annotation>> LOCK_OPERATION_ANNOTATIONS = new LinkedHashSet<>(8);

    private static final String KEY = "key";

    private static final String TIMEOUT = "timeout";

    private static final String TIME_UNIT = "timeUnit";

    private static final String CONDITION = "condition";

    private static final String PREFIX = "prefix";

    static {
        LOCK_OPERATION_ANNOTATIONS.add(Lock.class);
    }

    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        return AnnotationUtils.isCandidateClass(targetClass, LOCK_OPERATION_ANNOTATIONS);
    }

    @Override
    public LockAttribute parseLockAnnotation(AnnotatedElement element) {
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(element, Lock.class, false, false);
        return attributes != null ? parseLockAnnotation(attributes) : null;
    }

    private LockAttribute parseLockAnnotation(AnnotationAttributes element) {
        DefaultLockAttribute defaultLockAttribute = new DefaultLockAttribute();
        defaultLockAttribute.setTimeout(element.getNumber(TIMEOUT));
        defaultLockAttribute.setTimeUnit((TimeUnit) element.get(TIME_UNIT));
        defaultLockAttribute.setCondition(element.getString(CONDITION));
        defaultLockAttribute.setPrefix(element.getString(PREFIX));
        defaultLockAttribute.setKey(element.getAnnotationArray(KEY, Key.class));
        check(defaultLockAttribute);
        return defaultLockAttribute;
    }

    private void check(LockAttribute lockAttribute) throws IllegalStateException {
        Assert.state(lockAttribute.getKey().length > 0, "如果不使用锁,请删除@Lock注解");
        Arrays.stream(lockAttribute.getKey()).forEach(key -> {
            Assert.state(!StringUtils.isEmpty(key.name()), "org.designer.lock.annotation.Key.name 不能为空");
        });
    }

}
