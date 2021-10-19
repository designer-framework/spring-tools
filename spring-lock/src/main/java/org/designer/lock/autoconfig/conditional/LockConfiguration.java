package org.designer.lock.autoconfig.conditional;

import org.designer.lock.LockType;
import org.designer.lock.autoconfig.DefaultLockCreatorAutoConfig;
import org.designer.lock.autoconfig.RedissonLockCreatorAutoConfig;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/19 22:12
 */
public class LockConfiguration {

    private static final Map<LockType, Class<?>> MAPPINGS;

    static {
        Map<LockType, Class<?>> mappings = new EnumMap<>(LockType.class);
        mappings.put(LockType.REDISSON, RedissonLockCreatorAutoConfig.class);
        mappings.put(LockType.NONE, DefaultLockCreatorAutoConfig.class);
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private LockConfiguration() {
    }

    static String getConfigurationClass(LockType lockType) {
        Class<?> configurationClass = MAPPINGS.get(lockType);
        Assert.state(configurationClass != null, () -> "Unknown cache type " + lockType);
        return configurationClass.getName();
    }

    static LockType getType(String configurationClassName) {
        for (Map.Entry<LockType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unknown configuration class " + configurationClassName);
    }

}
