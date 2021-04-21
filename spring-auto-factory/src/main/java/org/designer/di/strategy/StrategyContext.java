package org.designer.di.strategy;

import org.designer.di.FieldMetaInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 15:12
 */
public class StrategyContext implements ApplicationContextAware, InitializingBean {

    @Autowired
    private Map<String, NamingFinderStrategy> strategy;

    /**
     * 寻找beanName的策略
     *
     * @param metaContext
     * @return
     */
    public NamingFinderStrategy matchStrategy(FieldMetaInfo.MetaContext metaContext) {
        return strategy.values().stream()
                .filter(t -> t.support(metaContext))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("strategy: " + metaContext));
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
