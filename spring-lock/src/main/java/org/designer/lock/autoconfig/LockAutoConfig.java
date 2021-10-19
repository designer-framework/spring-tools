package org.designer.lock.autoconfig;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.namming.NameMapper;
import org.designer.lock.namming.impl.DefaultNameMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/19 22:00
 * @see org.designer.lock.config.ProxyLockConfig#lockInterceptor(AnnotationLockCreator, NameMapper)
 */
@Configuration(proxyBeanMethods = false)
public class LockAutoConfig {

    /**
     * @param
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(NameMapper.class)
    NameMapper redissonNameMapper() {
        return new DefaultNameMapper();
    }

}