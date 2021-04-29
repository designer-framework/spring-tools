package org.designer.web.encryption.annotation.encrypt;

import org.designer.web.encryption.enums.SHAEncryptTypeEnum;

import java.lang.annotation.*;

/**
 * @author Designer
 * @see EncryptBody
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SHAEncryptBody {


    SHAEncryptTypeEnum value() default SHAEncryptTypeEnum.SHA256;

}
