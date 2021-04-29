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
public class VerifyBeanInfo extends AbstractVerifyBeanInfo implements VerifyInfo {

    private static final long serialVersionUID = 5884953589877721314L;

    public VerifyBeanInfo(VerifyInfo verifyInfo) {
        super(verifyInfo);
    }

    @Override
    public EncryptInfo convertEncryptInfo() {
        return new EncryptBeanInfo(this);
    }
}
