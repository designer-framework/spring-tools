package org.designer.codec.utils;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/25 21:04
 * @description 加解密签名
 */

public interface Sign<R, S> extends GetSign<S>, SetSign<R, S> {
}

