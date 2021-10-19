package org.designer.lock.namming.impl;

import org.designer.lock.namming.NameMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * @description: 锁的key生成工具
 * @author: Designer
 * @date : 2021/10/11 13:48
 */
public class DefaultNameMapper implements NameMapper {

    @Value(value = "${spring.application.name:default}")
    private String appName;

    public DefaultNameMapper() {
    }

    /**
     * @param prefix
     * @param key    可以在此处进行序列化
     * @return
     */
    @Override
    public String create(String prefix, Object key) {
        if (StringUtils.hasText(prefix)) {
            return appName + ":" + prefix + ":" + key;
        }
        return appName + ":" + key;
    }

}
