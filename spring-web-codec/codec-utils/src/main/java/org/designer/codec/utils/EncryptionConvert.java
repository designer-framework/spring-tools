package org.designer.codec.utils;

import org.designer.codec.utils.content.Convert;
import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/25 21:23
 * @description
 */

public interface EncryptionConvert {

    <R extends SetSign<R, S>, S> R encrypt(R content, EncryptInfo encryptInfo, Convert<R, EncryptInfo, S> convert);

    <R extends GetSign<S>, S> R decrypt(R content, DecryptInfo decryptInfo, Convert<S, DecryptInfo, R> convert);

    /**
     * 验签
     */
    <R extends GetSign<S>, S> Boolean verify(R content, VerifyInfo verifyInfo, Convert<R, VerifyInfo, S> convert);

}

