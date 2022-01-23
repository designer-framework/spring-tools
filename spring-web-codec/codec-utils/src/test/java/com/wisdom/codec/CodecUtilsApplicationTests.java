package com.wisdom.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.designer.codec.utils.EncryptionConvertUtil;
import org.designer.codec.utils.Sign;
import org.designer.codec.utils.SignTypeEnum;
import org.designer.codec.utils.config.DefaultEncryption;
import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptBeanInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;
import org.designer.codec.utils.utils.Encryption;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@Data
@Log4j2
@Accessors(chain = true)
/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ControllerCodecApplication.class})*/
public class CodecUtilsApplicationTests {

    public static final String signType = SignTypeEnum.BASE64.name();

    public static String sign;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private DefaultEncryption defaultEncryption;

    public static void toJson() {
        Inner inner = new Inner();
        log.info(JSON.toJSONString(inner));
    }

    @Test
    public void testDecrypt() {
        Inner inner = new Inner();
        inner.setSign(sign);
        Inner decrypt1 = new EncryptionConvertUtil(applicationContext).decrypt(inner, newDecryptInfo(), (content, decryptInfo) -> {
                    Encryption encrypt = defaultEncryption.getEncrypt(decryptInfo.getSignType());
                    String decrypt = encrypt.decrypt(content, decryptInfo);
                    return JSON.toJavaObject(JSONObject.parseObject(decrypt), inner.getClass());
                }
        );
        log.info("解密原文成功:" + JSON.toJSONString(decrypt1));
       /* EncryptionConvertUtil.verify(new VerifyInfo.DefaultVerifyInfo(SignTypeEnum.MD5.name()
                , new CommonApplicationTest(), null, "", "", "13E43F9355B110681EBBC3151AA7AA5A", null));*/
    }

    public EncryptInfo newEncryptInfo() {
        return new EncryptInfo() {
            private static final long serialVersionUID = 4631396640595938526L;

            @Override
            public String getSignType() {
                return signType;
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
            public String getCharset() {
                return "UTF-8";
            }

            @Override
            public String getSalt() {
                return null;
            }
        };

    }

    public VerifyInfo newVerifyInfo() {
        return new VerifyInfo() {
            private static final long serialVersionUID = 4631396640595938526L;

            @Override
            public String getSign() {
                return sign;
            }

            @Override
            public EncryptInfo convertEncryptInfo() {
                return new EncryptBeanInfo(this);
            }

            @Override
            public String getSignType() {
                return signType;
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
            public String getCharset() {
                return "UTF-8";
            }

            @Override
            public String getSalt() {
                return null;
            }
        };

    }

    public DecryptInfo newDecryptInfo() {
        return new DecryptInfo() {
            private static final long serialVersionUID = 4631396640595938526L;

            @Override
            public String getSign() {
                return sign;
            }

            @Override
            public String getSignType() {
                return signType;
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
            public String getCharset() {
                return "UTF-8";
            }

            @Override
            public String getSalt() {
                return null;
            }
        };

    }

    @Getter
    public static class Inner implements Sign<Inner, String> {

        private final int a = 1;

        private final int b = 1;

        private final int d = 1;

        private final String tempParam = "测试用例";

        private String sign;

        @Override
        public String getSign() {
            return sign;
        }

        @Override
        public Inner setSign(String sign) {
            this.sign = sign;
            return Inner.this;
        }

    }


}

