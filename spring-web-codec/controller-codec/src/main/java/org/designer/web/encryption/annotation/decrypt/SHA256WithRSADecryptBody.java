package org.designer.web.encryption.annotation.decrypt;

import org.designer.web.encryption.annotation.encrypt.EncryptBody;

import java.lang.annotation.*;

/**
 * @author Designer
 * @see EncryptBody
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SHA256WithRSADecryptBody {

    /**
     * 解密需要的key
     *
     * @return
     */
    String otherKey() default "";

}
