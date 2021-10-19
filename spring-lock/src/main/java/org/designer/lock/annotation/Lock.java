package org.designer.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: Designer
 * @date : 2021/9/19 1:35
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * SpEL表达式返回true才会执行加锁流程, 默认执行加锁流程
     *
     * @return
     */
    String condition() default "";

    /**
     * 锁前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 建议
     * 支持el表达式, 与@Cachable注解中的el表达式用法一模一样
     *
     * @return
     */
    Key[] key();

    /**
     * 获取锁的超时时间
     *
     * @return
     */
    int timeout() default 30;

    /**
     * 超时时间单位, 默认为秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * key参数写法和@Cachable注解的用法一模一样
     *
     * @return
     */
    String desc() default "注释: 超过指定的时间仍未获取到锁则抛出异常!如果timeout设置为-1,会一直等,直至获取到锁, key参数的写法和@Cachable注解的用法一模一样[SpringEL表达式]!";

}
