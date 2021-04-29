package org.designer.web.encryption.controller;

import lombok.Data;
import lombok.ToString;
import org.designer.web.encryption.annotation.decrypt.DecryptBody;
import org.designer.web.encryption.annotation.encrypt.EncryptBody;
import org.designer.web.encryption.enums.DecryptBodyMethodEnum;
import org.designer.web.encryption.enums.EncryptBodyMethodEnum;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Designer
 * @version 1.0.0
 * @date 2020/3/13 12:52
 * @description 测试加解密
 */
@EncryptBody(value = EncryptBodyMethodEnum.SHA256withRSA)
@DecryptBody(value = DecryptBodyMethodEnum.SHA256WithRSA)
@RestController
public class ControllerRest {

    @RequestMapping(value = "/en")
    public Inner a(@RequestBody Inner inner) {
        System.out.println(inner.toString());
        return inner;
    }

    @Data
    @ToString
    public static class Inner {

        private String data;
        private InnerInner innerInner;


        @Data
        @ToString
        public static class InnerInner {
            private String sign;
            private String data;
            private InnerInner1 innerInner;

            @Data
            @ToString
            public static class InnerInner1 {
                private String cdata;
                private String sign;
                private String data;
            }
        }
    }
}
