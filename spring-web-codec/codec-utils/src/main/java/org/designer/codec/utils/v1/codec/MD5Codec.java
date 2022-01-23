package org.designer.codec.utils.v1.codec;

import org.apache.commons.codec.digest.DigestUtils;
import org.designer.codec.utils.v1.builder.CodecParam;
import org.designer.codec.utils.v1.core.Codec;

/**
 * @description:
 * @author: Designer
 * @date : 2022/1/22 14:52
 */
public class MD5Codec implements Codec {

    @Override
    public String encryption(String data, CodecParam codecParam) {
        throw new RuntimeException(data);
    }

    @Override
    public String decryption(String data, CodecParam codecParam) {
        throw new RuntimeException(data);
    }

    @Override
    public String sign(String data, CodecParam codecParam) {
        return DigestUtils.md5Hex(data);
    }

    @Override
    public boolean verify(String data, CodecParam codecParam) {
        String sign = codecParam.getSign();
        return sign != null && sign.equalsIgnoreCase(DigestUtils.md5Hex(data));
    }

}
