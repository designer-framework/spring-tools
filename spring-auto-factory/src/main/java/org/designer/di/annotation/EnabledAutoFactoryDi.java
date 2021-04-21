package org.designer.di.annotation;

import org.designer.di.configuration.ImportFactoryDiSelector;
import org.designer.di.enums.NamingType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 20:34
 */
@Component
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackages = {"org.designer"})
@Import(value = {ImportFactoryDiSelector.class})
@EnableAutoConfiguration
public @interface EnabledAutoFactoryDi {

    boolean enable() default false;

    /**
     * bean名字生成策略
     *
     * @return
     */
    NamingType type() default NamingType.CUSTOMIZE;

}
