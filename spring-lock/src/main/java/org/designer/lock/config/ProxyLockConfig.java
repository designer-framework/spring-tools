package org.designer.lock.config;

import org.designer.lock.AnnotationLockCreator;
import org.designer.lock.ProxyLockProperties;
import org.designer.lock.advisor.BeanFactoryLockOperationSourceAdvisor;
import org.designer.lock.interceptor.DefaultLockAttributeSource;
import org.designer.lock.interceptor.LockAttributeSource;
import org.designer.lock.interceptor.LockInterceptor;
import org.designer.lock.namming.NameMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:50
 * @see org.springframework.cache.annotation.ProxyCachingConfiguration
 */
@Role(2)
@Configuration
@Import(ProxyLockProperties.class)
public class ProxyLockConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    LockAttributeSource lockAttributeSource() {
        return new DefaultLockAttributeSource();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    LockInterceptor lockInterceptor(
            AnnotationLockCreator annotationLockCreator
            , NameMapper nameMapper
    ) {
        LockInterceptor lockInterceptor = new LockInterceptor(annotationLockCreator, nameMapper);
        lockInterceptor.setLockAttributeSource(lockAttributeSource());
        return lockInterceptor;
    }


    @Bean(
            name = {"org.springframework.lock.config.internalLockAdvisor"}
    )
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    BeanFactoryLockOperationSourceAdvisor beanFactoryLockOperationSourceAdvisor(LockInterceptor lockInterceptor) {
        BeanFactoryLockOperationSourceAdvisor lockOperationSourceAdvisor = new BeanFactoryLockOperationSourceAdvisor();
        lockOperationSourceAdvisor.setAdvice(lockInterceptor);
        lockOperationSourceAdvisor.setLockAttributeSource(lockAttributeSource());
        return lockOperationSourceAdvisor;
    }

}
