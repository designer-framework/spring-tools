package org.designer.di.handler;

import lombok.extern.log4j.Log4j2;
import org.designer.di.FieldMetaInfo;
import org.designer.di.strategy.NamingFinderStrategy;
import org.designer.di.strategy.StrategyContext;
import org.designer.di.strategy.impl.ThreadLocalNamingFinder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/16 16:28
 */
@Log4j2
public abstract class AbstractInvokeHandler implements InvokeHandler, BeanFactoryAware {

    private final Map<String, FutureTask<Object>> beanCache = new ConcurrentHashMap<>(4);

    protected ConfigurableListableBeanFactory beanFactory;

    @Override
    public InvocationHandler getInvocationHandler(FieldMetaInfo fieldMetaInfo) {
        validField(fieldMetaInfo);
        return (proxy, method, args) -> {
            try {
                FieldMetaInfo.MetaContext metaContext = FieldMetaInfo.MetaContext.builder()
                        .fieldMetaInfo(fieldMetaInfo)
                        .args(args)
                        .build();
                NamingFinderStrategy beanNameStrategy = beanFactory.getBean(StrategyContext.class).matchStrategy(metaContext);
                String supportType = beanNameStrategy.findSupportType(metaContext);
                Object bean = getBeanByType(supportType, metaContext);
                if (bean == null) {
                    throw new IllegalStateException("未找到实现类" + metaContext);
                }
                return method.invoke(bean, args);
            } finally {
                ThreadLocalNamingFinder.remove();
            }
        };
    }

    protected abstract Object getBeanByType(String supportType, FieldMetaInfo.MetaContext metaContext) throws Exception;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    protected void validField(FieldMetaInfo fieldMetaInfo) {
        if (fieldMetaInfo.getFactory().required() && getBean(fieldMetaInfo.getField().getType()).isEmpty()) {
            throw new IllegalStateException(fieldMetaInfo.getField().getType().getName() + " 实例未创建");
        }
    }

    protected <T> Map<String, T> getBean(Class<T> type) {
        return beanFactory.getBeansOfType(type);
    }

    protected <T> Map<String, T> getBean(Class<T> type, Class<T>[] exclude) {
        Map<String, T> factoryBeans = beanFactory.getBeansOfType(type);
        Map<String, T> myBeans = new HashMap<>(4);
        factoryBeans.forEach((key, value) -> {
            if (exclude.length > 0) {
                Arrays.stream(exclude).forEach(excludeClass -> {
                    if (value.getClass() != excludeClass) {
                        myBeans.put(key, value);
                    }
                });
            } else {
                myBeans.putAll(factoryBeans);
            }
        });
        return myBeans;
    }

    /**
     * @param beanName
     * @param fieldMetaInfo
     * @return
     */
    protected Object getBean(String beanName, FieldMetaInfo fieldMetaInfo) throws BeanCreationException {
        FutureTask<Object> task = beanCache.get(beanName);
        if (task == null) {
            FutureTask<Object> objectFutureTask = new FutureTask<>(() -> {
                return getBean(fieldMetaInfo.getField().getType()).get(beanName);
            });
            task = beanCache.putIfAbsent(beanName, objectFutureTask);
            if (task == null) {
                task = objectFutureTask;
            }
            task.run();
        }
        try {
            return task.get();
        } catch (InterruptedException | CancellationException | ExecutionException e) {
            beanCache.remove(beanName);
            throw new BeanCreationException(beanName, e);
        }
    }


}
