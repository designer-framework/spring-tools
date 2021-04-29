package org.designer.codec.utils;

/**
 * @author huangzy
 * @Description: 签名类型
 * @date 2019/12/1 14:45
 */
public enum SignTypeEnum {

    MD5,
    SHA256,
    RSA2,
    SHA256WithRSA,
    BASE64;


    public static SignTypeEnum checkName(String name) {
        for (SignTypeEnum value : values()) {
            if (value.toString().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
