package org.designer.web.encryption.annotation.encrypt;


import org.designer.web.encryption.enums.EncryptBodyMethodEnum;
import org.designer.web.encryption.enums.SHAEncryptTypeEnum;

import java.lang.annotation.*;

/**
 * <p>加密{@link org.springframework.web.bind.annotation.ResponseBody}响应数据，可用于整个控制类或者某个控制器上</p>
 *
 * @author Designer
 * @version 2020-03-09
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptBody {

    EncryptBodyMethodEnum value() default EncryptBodyMethodEnum.MD5;

    /**
     * 加密需要的key
     *
     * @return
     */
    String otherKey() default "";

    SHAEncryptTypeEnum shaType() default SHAEncryptTypeEnum.SHA256;

}
