package org.designer.codec.utils.v1.core;

import org.designer.codec.utils.v1.builder.CodecParam;

/**
 * @description:
 * @author: Designer
 * @date: 2022/1/22 14:34
 */
public interface Codec {

    /**
     * 加密
     *
     * @return
     */
    String encryption(String data, CodecParam codecParam);

    /**
     * 解密
     *
     * @return
     */
    String decryption(String data, CodecParam codecParam);

    /**
     * 取摘要
     *
     * @return
     */
    String sign(String data, CodecParam codecParam);

    /**
     * 校验
     *
     * @return
     */
    boolean verify(String data, CodecParam codecParam);

}
