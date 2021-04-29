package org.designer.common.annotation;

import java.lang.annotation.*;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.annotation
 * @Author: Designer
 * @CreateTime: 2021-04-24 21
 * @Description:
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}
