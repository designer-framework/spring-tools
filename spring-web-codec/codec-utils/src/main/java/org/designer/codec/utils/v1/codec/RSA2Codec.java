package org.designer.codec.utils.v1.codec;

import org.designer.codec.utils.v1.builder.CodecParam;
import org.designer.codec.utils.v1.core.Codec;
import org.designer.codec.utils.v1.utils.RSA2Util;

/**
 * @description:
 * @author: Designer
 * @date : 2022/1/22 14:52
 */
public class RSA2Codec implements Codec {

    @Override
    public String encryption(String data, CodecParam codecParam) {
        return RSA2Util.buildRSAEncryptByPrivateKey(data, codecParam.getPrivateKey());
    }

    @Override
    public String decryption(String data, CodecParam codecParam) {
        return RSA2Util.buildRSADecryptByPublicKey(data, codecParam.getPublicKey());
    }

    @Override
    public String sign(String data, CodecParam codecParam) {
        return RSA2Util.buildRSASignByPrivateKey(data, codecParam.getPrivateKey());
    }

    @Override
    public boolean verify(String data, CodecParam codecParam) {
        return RSA2Util.buildRSAVerifyByPublicKey(data, codecParam.getPublicKey(), codecParam.getSign());
    }

}
