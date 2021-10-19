package org.designer.lock;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 21:50
 * @see org.springframework.cache.annotation.ProxyCachingConfiguration
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.lock")
public class ProxyLockProperties {

    private LockType type = LockType.NONE;

}
