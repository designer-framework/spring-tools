package org.designer.codec.utils.v1.builder;

import lombok.Builder;
import lombok.Getter;

/**
 * @description:
 * @author: Designer
 * @date : 2022/1/22 14:41
 */
@Getter
@Builder
public class CodecParam {

    /**
     * 签名类型
     */
    protected String signType;

    /**
     * 公钥
     */
    protected String publicKey;

    /**
     * 私钥
     */
    protected String privateKey;

    /**
     * 盐
     */
    protected String salt;

    /**
     * 字符编码
     */
    protected String charset;

    /**
     * 签名
     */
    protected String sign;

}
