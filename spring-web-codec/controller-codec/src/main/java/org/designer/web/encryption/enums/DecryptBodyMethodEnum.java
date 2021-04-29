package org.designer.web.encryption.enums;


/**
 * @author Designer
 * @version V1.0.0
 * @date 2020/3/13 13:06
 * @description 解密传入的数据的加密类型
 */
public enum DecryptBodyMethodEnum implements SecurityEnumInterface {

    DES, AES, RSA, SHA256WithRSA;

    @Override
    public String getEncryptionType() {
        return name();
    }

}
