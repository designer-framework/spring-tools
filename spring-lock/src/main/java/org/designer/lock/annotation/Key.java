package org.designer.lock.annotation;

import java.lang.annotation.*;

/**
 * @author: Designer
 * @date : 2021/9/19 1:35
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {

    /**
     * 锁前缀, 如果此值为空, 则从@Lock注解获取
     *
     * @return
     */
    String prefix() default "";

    /**
     * 锁名字
     *
     * @return
     */
    String name();

}
