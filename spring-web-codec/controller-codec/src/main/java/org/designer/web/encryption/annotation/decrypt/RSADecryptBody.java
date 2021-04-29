package org.designer.web.encryption.annotation.decrypt;

import java.lang.annotation.*;


@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RSADecryptBody {
}
