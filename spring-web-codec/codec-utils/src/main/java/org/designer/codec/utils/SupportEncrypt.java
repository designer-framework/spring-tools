package org.designer.codec.utils;

import org.designer.codec.utils.utils.Encryption;

import java.util.Map;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 23:49
 * @description 加密方式拓展
 */
@FunctionalInterface
public interface SupportEncrypt {

    Map<String, Encryption> supportEncryptions();

}
