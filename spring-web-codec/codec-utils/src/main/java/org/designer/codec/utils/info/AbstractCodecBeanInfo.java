package org.designer.codec.utils.info;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public abstract class AbstractCodecBeanInfo implements CodecBaseInfo {

    private static final long serialVersionUID = -2488641171866509257L;

    protected String objToStrType;

    protected String signType;

    protected String charset;

    protected String privateKey;

    protected String publicKey;

    protected String salt;

    public AbstractCodecBeanInfo(CodecBaseInfo codecBaseInfo) {
        charset = codecBaseInfo.getCharset();
        privateKey = codecBaseInfo.getPrivateKey();
        publicKey = codecBaseInfo.getPublicKey();
        salt = codecBaseInfo.getSalt();
        signType = codecBaseInfo.getSignType();
        objToStrType = codecBaseInfo.getSignType();
    }

    @Override
    public String getObjToStringType() {
        return objToStrType;
    }

    @Override
    public String getCharset() {
        return charset == null ? PkgConstant.DEFAULT_CHARSET : charset;
    }

    @Override
    public String getSalt() {
        return salt;
    }

}
