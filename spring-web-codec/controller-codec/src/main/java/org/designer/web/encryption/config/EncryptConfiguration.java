package org.designer.web.encryption.config;

import org.designer.web.encryption.util.DefaultBeanConvertToParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/28 16:54
 */
@Configuration
public class EncryptConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {EncryptBodyProperty.class})
    public EncryptBodyProperty encryptBodyProperty() {
        return new InnerEncryptBodyProperty();
    }

    private static class InnerEncryptBodyProperty implements EncryptBodyProperty {

        public InnerEncryptBodyProperty() {
            throw new IllegalStateException("请实现 [" + getClass().getInterfaces()[0].getName() + "] 接口!");
        }

        @Override
        public String getPrivateKey() {
            return null;
        }

        @Override
        public String getPublicKey() {
            return null;
        }

        @Override
        public String convertRequestJsonToString(String requestJsonString) {
            return null;
        }

        @Override
        public String convertResponseBodyToString(Object responseObj) {
            return new DefaultBeanConvertToParam().generateParamStr(responseObj);
        }

        @Override
        public Boolean encryptRestSupport(Object responseObj) {
            return null;
        }

        @Override
        public <R, T> T writeSignToBody(R restBody, String sign) {
            return null;
        }
    }

}
