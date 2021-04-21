package org.designer.di.annotation;

import org.designer.di.enums.InvokeHandlerType;
import org.designer.di.strategy.NamingFinderStrategy;

import java.lang.annotation.*;

/**
 * @description: 实现该接口即可实现对代理属性的注入
 * @author: Designer
 * @date : 2021/4/15 16:09
 */
@Inherited
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Factory {

    /**
     * 通过该策略类实现对Bean的精确查找
     *
     * @return 具体名字查找
     */
    Class<? extends NamingFinderStrategy> nameStrategy() default NamingFinderStrategy.class;

    /**
     * 是否不能为空
     *
     * @return
     */
    boolean required() default true;

    /**
     * 目前仅支持一种代理
     *
     * @return
     */
    InvokeHandlerType type() default InvokeHandlerType.MATCH_INVOKE;


    /**
     * @return
     */
    MatchInvoke matchInvoke() default @MatchInvoke();

    /**
     * 哪些不需要注入到代理对象
     *
     * @return
     */
    @Deprecated
    Class[] exclude() default {};

}
