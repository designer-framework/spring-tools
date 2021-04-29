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
public abstract class AbstractVerifyBeanInfo extends AbstractCodecBeanInfo {

    private static final long serialVersionUID = -2488641171866509257L;

    private String sign;

    public AbstractVerifyBeanInfo(VerifyInfo verifyInfo) {
        super(verifyInfo);
        sign = verifyInfo.getSign();
    }


}
