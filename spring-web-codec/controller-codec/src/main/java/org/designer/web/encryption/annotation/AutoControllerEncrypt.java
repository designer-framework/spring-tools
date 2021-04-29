package org.designer.web.encryption.annotation;

import org.designer.web.encryption.advice.DecryptRequestBodyAdvice;
import org.designer.web.encryption.advice.EncryptResponseBodyAdvice;
import org.designer.web.encryption.advice.SingletonJacksonMapper;
import org.designer.web.encryption.config.EncryptBodyProperty;
import org.designer.web.encryption.config.HttpConverterConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

/**
 * feihao-security
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/8 12:16
 * @description 自动加载
 */
@ConditionalOnBean(value = EncryptBodyProperty.class)
@ConditionalOnProperty(prefix = "encrypt.body", name = "enable", havingValue = "true")
@Import(value = {DecryptRequestBodyAdvice.class, EncryptResponseBodyAdvice.class, SingletonJacksonMapper.class, HttpConverterConfig.class})
public class AutoControllerEncrypt {

    public AutoControllerEncrypt() {
    }

}
