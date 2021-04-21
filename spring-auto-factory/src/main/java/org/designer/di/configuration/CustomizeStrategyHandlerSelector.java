package org.designer.di.configuration;

import org.designer.di.handler.InvokeHandler;
import org.designer.di.strategy.NamingFinderStrategy;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 23:24
 */
public class CustomizeStrategyHandlerSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> beans = SpringFactoriesLoader.loadFactoryNames(InvokeHandler.class, ClassUtils.getDefaultClassLoader());
        beans.addAll(
                SpringFactoriesLoader.loadFactoryNames(NamingFinderStrategy.class, ClassUtils.getDefaultClassLoader())
        );
        return beans.toArray(new String[0]);

    }

}
