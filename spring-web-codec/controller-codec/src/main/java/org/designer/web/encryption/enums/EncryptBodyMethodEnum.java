package org.designer.web.encryption.enums;


public enum EncryptBodyMethodEnum implements SecurityEnumInterface {

    MD5, DES, AES, SHA, RSA, SHA256withRSA;

    @Override
    public String getEncryptionType() {
        return name();
    }
}
