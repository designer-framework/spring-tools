package org.designer.codec.utils.info;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 0:31
 * @description
 */
@Getter
@Log4j2
public class EncryptBeanInfo extends AbstractEncryptBeanInfo implements EncryptInfo {

    private static final long serialVersionUID = 3414684865375915853L;

    public EncryptBeanInfo(EncryptInfo encryptInfo) {
        super(encryptInfo);
    }

    public EncryptBeanInfo(VerifyInfo verifyInfo) {
        super(verifyInfo);
    }
}
