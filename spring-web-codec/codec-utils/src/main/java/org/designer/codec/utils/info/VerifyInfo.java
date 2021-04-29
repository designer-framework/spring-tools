package org.designer.codec.utils.info;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/19 3:09
 * @description
 */

public interface VerifyInfo extends CodecBaseInfo {

    /**
     * 密文或者签名值
     *
     * @return
     */
    String getSign();

    /**
     * @return
     */
    EncryptInfo convertEncryptInfo();


}