package org.designer.web.encryption.annotation.decrypt;

import org.designer.web.encryption.enums.DecryptBodyMethodEnum;

import java.lang.annotation.*;

/**
 * @author Designer
 * @version V1.0.0
 * @date 2020/3/13 13:08
 * @description 解密注解
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptBody {

    /**
     * 解密的方式
     *
     * @return
     */
    DecryptBodyMethodEnum value() default DecryptBodyMethodEnum.SHA256WithRSA;

    /**
     * 解密需要的key
     *
     * @return
     */
    String otherKey() default "";

}
