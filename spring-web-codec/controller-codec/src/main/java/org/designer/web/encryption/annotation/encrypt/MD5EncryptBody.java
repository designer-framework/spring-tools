package org.designer.web.encryption.annotation.encrypt;

import java.lang.annotation.*;

/**
 * @author Designer
 * @version 2020-03-09
 * @see EncryptBody
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MD5EncryptBody {
}
