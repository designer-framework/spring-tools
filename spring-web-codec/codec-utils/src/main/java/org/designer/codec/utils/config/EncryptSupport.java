package org.designer.codec.utils.config;

import org.designer.codec.utils.utils.Encryption;

import java.util.Map;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/25 18:35
 * @description
 */

public interface EncryptSupport {

    void supportEncryptions(Map<String, Encryption> encryptionMap);

}

