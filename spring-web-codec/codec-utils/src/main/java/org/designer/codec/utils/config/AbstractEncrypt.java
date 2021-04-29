package org.designer.codec.utils.config;

import org.designer.codec.utils.SignTypeEnum;
import org.designer.codec.utils.utils.*;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/25 18:35
 * @description
 */

public abstract class AbstractEncrypt implements Encrypt {

    private static final Map<String, Encryption> ENCRYPTION_DELEGATE = new ConcurrentHashMap<>();

    static {
        ENCRYPTION_DELEGATE.put(SignTypeEnum.MD5.name(), new MD5Util());
        ENCRYPTION_DELEGATE.put(SignTypeEnum.RSA2.name(), new RSA2Util());
        ENCRYPTION_DELEGATE.put(SignTypeEnum.SHA256.name(), new SHA256Utils());
        ENCRYPTION_DELEGATE.put(SignTypeEnum.SHA256WithRSA.name(), new MD5Util());
        ENCRYPTION_DELEGATE.put(SignTypeEnum.BASE64.name(), new BASE64Utils());
    }

    public void supportEncryption(Map<String, Encryption> encryptionMap) {
        Optional.ofNullable(encryptionMap).orElseGet(() -> new HashMap<>(0)).entrySet().parallelStream()
                .forEach(encryptionEntry -> {
                    Assert.notNull(encryptionEntry.getValue(), "ignore encrypt" + encryptionEntry.getKey());
                    ENCRYPTION_DELEGATE.entrySet().add(encryptionEntry);
                });
    }

    @Override
    public Encryption getEncrypt(String signType) {
        if (ENCRYPTION_DELEGATE.containsKey(signType)) {
            return ENCRYPTION_DELEGATE.get(signType);
        }
        throw new IllegalArgumentException(String.format("未实现%s加密", signType));
    }

}

