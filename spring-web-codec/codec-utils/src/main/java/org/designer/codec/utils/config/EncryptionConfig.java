package org.designer.codec.utils.config;

import org.designer.codec.utils.DefaultBeanConvertToParam;
import org.designer.codec.utils.EncryptionConvert;
import org.designer.codec.utils.EncryptionConvertUtil;
import org.designer.codec.utils.SupportEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.List;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 23:40
 * @description
 */
public class EncryptionConfig {

    /**
     * @param supportEncrypt 用户自行实现的加解密方式
     * @return 系统支持的加密方式+ 用户自行实现的加密方式
     */
    @Bean
    public Encrypt encryptionUtils(@Autowired(required = false) List<SupportEncrypt> supportEncrypt) {
        DefaultEncryption defaultEncryption = new DefaultEncryption();
        if (supportEncrypt != null && !supportEncrypt.isEmpty()) {
            supportEncrypt.parallelStream().forEach(support -> {
                //增加用户自定义加解密支持
                defaultEncryption.supportEncryption(support.supportEncryptions());
            });
        }
        return defaultEncryption;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EncryptionConvert.class})
    public EncryptionConvert encryptionConvertUtil(ApplicationContext applicationContext) {
        return new EncryptionConvertUtil(applicationContext);
    }

    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnMissingBean(value = {DefaultBeanConvertToParam.class})
    public DefaultBeanConvertToParam defaultBeanConvertToParam() {
        return new DefaultBeanConvertToParam();
    }

}
