package org.designer.codec.utils.utils;


import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;

import java.util.Base64;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 20:10
 * @description
 */

public class BASE64Utils implements Encryption {

    public static final Base64.Decoder DECODER = Base64.getDecoder();

    public static final Base64.Encoder ENCODER = Base64.getEncoder();


    @Override
    public String encrypt(String content, EncryptInfo encryptInfo) {
        return ENCODER.encodeToString((content).getBytes());
    }

    @Override
    public String decrypt(String content, DecryptInfo decodeInfo) {
        return new String(DECODER.decode((content)));
    }

    @Override
    public boolean verify(String content, VerifyInfo verifyInfo) {
        return ENCODER.encodeToString((content).getBytes()).equals(verifyInfo.getSign());
    }

}
