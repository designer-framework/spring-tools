package org.designer.codec.utils.config;

import org.designer.codec.utils.utils.Encryption;

import java.util.Map;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 22:50
 * @description
 */
public class DefaultEncryption extends AbstractEncrypt {

    public DefaultEncryption() {
    }

    @Override
    public void supportEncryption(Map<String, Encryption> encryptionMap) {
        super.supportEncryption(encryptionMap);
    }

}
