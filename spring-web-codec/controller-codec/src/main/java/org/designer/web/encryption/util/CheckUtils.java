package org.designer.web.encryption.util;

import org.designer.web.encryption.exception.DecryptBodyFailException;
import org.designer.web.encryption.exception.KeyNotConfiguredException;

/**
 * @author Designer
 * @version V1.0.0
 * @date 2020/6/9 17:25
 * @description 控制层加密解密工具类
 */
public class CheckUtils {


    /**
     * 优先级最高的是方法注解上的Key，其次是自动注入的配置文件中的Key
     *
     * @param autoConfigKey 自动配置注入的Key
     * @param classKey      类文件中的key
     * @param keyName
     * @return
     */
    public static String checkAndGetKey(String autoConfigKey, String classKey, String keyName) {
        if (StringUtils.isNullOrEmpty(autoConfigKey) && StringUtils.isNullOrEmpty(classKey)) {
            throw new KeyNotConfiguredException(String.format("未配置%s", keyName));
        }
        if (classKey == null) {
            return autoConfigKey;
        }
        return classKey;
    }

    /**
     * 对外界上送的请求参数验签结果进行断言判断
     *
     * @param body
     * @param verifySuccess
     * @return
     */
    public static String verifySignAndReturn(String body, boolean verifySuccess) {
        if (!verifySuccess) {
            throw new DecryptBodyFailException();
        }
        return body;
    }

}
