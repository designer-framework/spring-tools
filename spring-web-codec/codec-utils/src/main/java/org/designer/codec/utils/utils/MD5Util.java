/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package org.designer.codec.utils.utils;

import lombok.SneakyThrows;
import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Util implements Encryption {

    private static final Logger log = LoggerFactory.getLogger(MD5Util.class);

    @SneakyThrows
    public static final String MD5(String content) {
        Assert.notNull(content, "content");
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                'F'};
        MessageDigest mdInst;

        byte[] arg10;
        try {
            arg10 = content.getBytes(StandardCharsets.UTF_8);
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException arg9) {
            throw new Exception("MD5加密失败", arg9);
        }

        mdInst.update(arg10);
        byte[] md = mdInst.digest();
        int j = md.length;
        char[] str = new char[j * 2];
        int k = 0;

        for (int i = 0; i < j; ++i) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 15];
            str[k++] = hexDigits[byte0 & 15];
        }

        return new String(str);
    }

    @SneakyThrows
    public static boolean verifyMD5(String content, String sign) {
        if (StringUtils.isEmpty(content)) {
            return false;
        } else {
            return sign.equals(MD5(content));
        }
    }

    /*public static String convertMD5(String decode) {
        char[] a = decode.toCharArray();
        for (int decodeStr = 0; decodeStr < a.length; ++decodeStr) {
            a[decodeStr] = (char) (a[decodeStr] ^ 116);
        }
        return new String(a);
    }*/

    @Override
    public String encrypt(String content, EncryptInfo encryptInfo) {
        return MD5(content);
    }

    @Override
    public String decrypt(String content, DecryptInfo decodeInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean verify(String content, VerifyInfo verifyInfo) {
        return verifyMD5(content, verifyInfo.getSign());
    }
}
