package org.designer.lock.autoconfig;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.autoconfig.conditional.LockCondition;
import org.designer.lock.impl.RedissonAnnotationLockCreator;
import org.redisson.api.RedissonClient;
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
public class RedissonLockCreatorAutoConfig {

    @Bean
    @ConditionalOnMissingBean(AnnotationLockCreator.class)
    AnnotationLockCreator annotationLockCreator(RedissonClient client) {
        return new RedissonAnnotationLockCreator(client);
    }

}