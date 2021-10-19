package org.designer.lock.autoconfig.conditional;

import lombok.extern.slf4j.Slf4j;
import org.designer.lock.LockType;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/19 22:04
 */
@Slf4j
public class LockCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = context.getEnvironment();
        try {
            BindResult<LockType> bind = Binder.get(environment).bind("spring.lock.type", LockType.class);
            ConditionMessage.Builder builder = ConditionMessage.forCondition("注解锁");
            AnnotationMetadata metadata = (AnnotationMetadata) annotatedTypeMetadata;
            if (bind.isBound()) {
                LockType lockType = LockConfiguration.getType(metadata.getClassName());
                if (lockType == bind.get()) {
                    return ConditionOutcome.match(builder.foundExactly(lockType.name()));
                }
                return ConditionOutcome.noMatch("注解锁类型不匹配");
            } else {
                if (LockConfiguration.getConfigurationClass(LockType.NONE).equals(metadata.getClassName())) {
                    return ConditionOutcome.match(builder.because("未配置锁类型, 启用了默认配置: " + LockType.NONE));
                } else {
                    return ConditionOutcome.noMatch("未知的注解锁类型");
                }
            }
        } catch (BindException exception) {
            ConfigurationProperty property = exception.getProperty();
            return ConditionOutcome.noMatch("注解锁类型错误: " + property.getName());
        }
    }

}
