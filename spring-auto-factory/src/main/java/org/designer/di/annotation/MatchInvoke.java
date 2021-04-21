package org.designer.di.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/19 10:01
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchInvoke {

    String method() default "match";

    boolean enable() default false;

    Class<?>[] argsType() default {String.class};

}
