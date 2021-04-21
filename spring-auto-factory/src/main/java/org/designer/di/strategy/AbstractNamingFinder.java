package org.designer.di.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @description: 默认策略实现
 * @author: Designer
 * @date : 2021/4/15 19:29
 */
public abstract class AbstractNamingFinder implements NamingFinderStrategy, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    protected abstract String doGetName(Object[] args);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

