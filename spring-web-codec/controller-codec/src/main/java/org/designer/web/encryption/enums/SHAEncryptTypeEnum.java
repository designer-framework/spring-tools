package org.designer.web.encryption.enums;

public enum SHAEncryptTypeEnum {

    SHA224("sha-224"),
    SHA256("sha-256"),
    SHA384("sha-384"),
    SHA512("sha-512"),
    ;

    public String value;

    SHAEncryptTypeEnum(String value) {
        this.value = value;
    }
}
