package org.designer.web.encryption.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/7 18:57
 * @description 加密消息体的一些配置消息，通过实现该接口进行定义
 * @see
 */

public interface EncryptBodyProperty {

    String SIGN_FIELD = "sign";

    /**
     * 加密私钥,
     * 1.可通过线程变量传递
     * 2.直接将yml配置类实现该接口，全局统一
     *
     * @return
     */
    String getPrivateKey();

    /**
     * 加密公钥
     * 1.可通过线程变量传递
     * 2.直接将yml配置类实现该接口，全局统一
     *
     * @return
     */
    String getPublicKey();

    /**
     * 签名字段
     *
     * @return
     */
    default String getSignField() {
        return SIGN_FIELD;
    }

    /**
     * 字符串编码
     *
     * @return
     */
    default Charset getEncoding() {
        return StandardCharsets.UTF_8;
    }

    /**
     * 将请求对象转换成待验签字符串
     *
     * @param requestJsonString
     * @return
     */
    String convertRequestJsonToString(String requestJsonString);

    /**
     * 将响应对象转换成待加密字符串
     *
     * @param responseObj
     * @return
     */
    String convertResponseBodyToString(Object responseObj);

    /**
     * 判断对象是否需要加密
     *
     * @param responseObj
     * @return
     */
    Boolean encryptRestSupport(Object responseObj);

    /**
     * 将签名放入对象中, 响应给调用方
     *
     * @param restBody
     * @param sign
     * @param <R>
     * @param <T>
     * @return
     */
    <R, T> T writeSignToBody(R restBody, String sign);


}

