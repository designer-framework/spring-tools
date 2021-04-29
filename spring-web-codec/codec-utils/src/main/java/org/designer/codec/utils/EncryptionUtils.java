package org.designer.codec.utils;

import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 20:26
 * @description 只负责对字符串的加密解密验签
 */

public interface EncryptionUtils {

    /**
     * 计算签名
     *
     * @param encryptInfo
     * @return
     */
    String encrypt(EncryptInfo encryptInfo) throws IllegalArgumentException;

    /**
     * 默认使用公钥进行密文解密
     *
     * @param decryptInfo
     * @return 原文
     */
    String decrypt(DecryptInfo decryptInfo) throws IllegalArgumentException;

    /**
     * 验签
     *
     * @param verifyInfo
     * @return
     */
    boolean verify(VerifyInfo verifyInfo) throws IllegalArgumentException;


}

