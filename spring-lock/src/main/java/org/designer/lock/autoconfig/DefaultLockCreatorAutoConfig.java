package org.designer.lock.autoconfig;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.LockFunction;
import org.designer.lock.autoconfig.conditional.LockCondition;
import org.designer.lock.interceptor.LockAttribute;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/19 22:00
 */
@Conditional(value = LockCondition.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AnnotationLockCreator.class)
public class DefaultLockCreatorAutoConfig {

    @Bean
    AnnotationLockCreator annotationLockCreator() {
        return new AnnotationLockCreator() {
            @Override
            public LockFunction getLock(Object key, LockAttribute lockAttribute) {
                throw new IllegalStateException("请重写 org.designer.lock.AnnotationLockCreator 接口并注入到容器");
            }
        };
    }

}