package org.designer.codec.utils.info;

import java.io.Serializable;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/19 3:12
 * @description
 */

public interface CodecBaseInfo extends Serializable {

    default String getObjToStringType() {
        return "SPLIT_APPEND_TO_STRING";
    }

    String getSignType();

    String getPrivateKey();

    String getPublicKey();

    String getCharset();

    String getSalt();

}