package org.designer.codec.utils.config;


import org.designer.codec.utils.utils.Encryption;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/25 18:35
 * @description 获取加解密方式
 */

public interface Encrypt {

    /**
     * @param signType 签名方式
     * @return 具体加解密实现
     */
    Encryption getEncrypt(String signType);

}

