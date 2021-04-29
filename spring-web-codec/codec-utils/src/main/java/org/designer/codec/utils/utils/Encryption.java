package org.designer.codec.utils.utils;

import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 20:26
 * @description 加密
 */

public interface Encryption {

    String encrypt(String content, EncryptInfo encryptInfo);

    /**
     * 默认使用公钥进行密文解密
     *
     * @param decryptInfo
     * @return 原文
     */
    String decrypt(String content, DecryptInfo decryptInfo);

    boolean verify(String content, VerifyInfo verifyInfo);

}

