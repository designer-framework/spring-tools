package org.designer.di;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.designer.di.annotation.Factory;
import org.springframework.beans.factory.annotation.InjectionMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 19:35
 */
@Getter
@ToString
@Builder
public class FieldMetaInfo {

    private final Factory factory;

    private final Object targetBean;

    /**
     * 在调用目标方法方法之前, 如果该方法不为空, 则会调用该方法进行校验
     */
    private final Method validMethod;

    private final InjectionMetadata.InjectedElement injectedElement;

    public Field getField() {
        return injectedElement.getMember() instanceof Field ? (Field) injectedElement.getMember() : null;
    }

    public boolean isField() {
        return injectedElement.getMember() instanceof Field;
    }

    public InjectionMetadata.InjectedElement getInjectedElement() {
        return injectedElement;
    }

    @Getter
    @ToString
    @Builder
    public static class MetaContext {

        /**
         * 被代理的字段属性,及校验参数
         */
        private final FieldMetaInfo fieldMetaInfo;

        /**
         * 动态方法参数
         */
        private final Object[] args;

        /**
         * 动态方法
         */
        private final Method method;

        public boolean requiredVerify() {
            return fieldMetaInfo.getFactory().matchInvoke().enable();
        }

        public boolean isVerifyMethod() {
            return method != null
                    && fieldMetaInfo.getValidMethod() != null
                    && method.getName().equals(fieldMetaInfo.getValidMethod().getName());
        }

        public Field getField() {
            return fieldMetaInfo.getField() != null ? fieldMetaInfo.getField() : null;
        }

    }

}
