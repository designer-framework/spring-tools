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
public abstract class AbstractDecryptBeanInfo extends AbstractCodecBeanInfo {

    private static final long serialVersionUID = -2488641171866509257L;

    private String sign;

    public AbstractDecryptBeanInfo(DecryptInfo decryptInfo) {
        super(decryptInfo);
        sign = decryptInfo.getSign();
    }

    /*public String getTargetContent() {
        if (PkgConstant.DEFAULT_CHARSET.equals(getCharset())) {
            return targetContent;
        }
        return new String(targetContent.getBytes(Charset.forName(getCharset())), Charset.defaultCharset());
    }*/


}
