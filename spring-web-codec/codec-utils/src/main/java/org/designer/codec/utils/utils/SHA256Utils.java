package org.designer.codec.utils.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 20:10
 * @description
 */
public class SHA256Utils implements Encryption {

    /**
     * sha256 加密
     *
     * @param content
     * @return
     */
    public static String encodeSHA256(String content) {
        return DigestUtils.sha256Hex(content).toUpperCase();
    }

    public static boolean verifySHA256(String content, String sign) {
        return DigestUtils.sha256Hex(content).equalsIgnoreCase(sign);
    }

    @Override
    public String encrypt(String content, EncryptInfo encryptInfo) {
        return encodeSHA256(content);
    }

    @Override
    public String decrypt(String content, DecryptInfo decryptInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean verify(String content, VerifyInfo verifyInfo) {
        return verifySHA256(content, verifyInfo.getSign());
    }

}
