package org.designer.codec.utils.annotation;

import org.designer.codec.utils.config.EncryptionConfigSelector;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/19 0:04
 * @description 自动化注入
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ComponentScan(value = {"org.designer"})
@Import(value = {EncryptionConfigSelector.class})
public @interface EnableEncryptTools {

    boolean enable() default true;

}
