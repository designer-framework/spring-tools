package org.designer.codec.utils.v1.utils;

import org.apache.commons.codec.digest.DigestUtils;


public class MD5EncryptUtil {

    /**
     * MD5加密-32位小写
     */
    public static String encrypt(String data) {
        return DigestUtils.md5Hex(data);
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("123456"));
        System.out.println(MD5EncryptUtil.encrypt("123456"));
    }

}
