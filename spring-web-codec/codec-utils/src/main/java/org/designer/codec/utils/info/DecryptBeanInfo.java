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
public class DecryptBeanInfo extends AbstractDecryptBeanInfo implements DecryptInfo {

    private static final long serialVersionUID = -2232969276820783742L;

    public DecryptBeanInfo(DecryptInfo decodeInfo) {
        super(decodeInfo);
    }

}
