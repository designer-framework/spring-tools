package org.designer.web.encryption.annotation.encrypt;

import java.lang.annotation.*;

/**
 * @author Designer
 * @see EncryptBody
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DESEncryptBody {

    /**
     * 加密需要的key
     *
     * @return
     */
    String otherKey() default "";

}
