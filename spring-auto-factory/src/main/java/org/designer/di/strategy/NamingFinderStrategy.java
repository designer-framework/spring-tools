package org.designer.di.strategy;

import org.designer.di.FieldMetaInfo;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 19:30
 */
public interface NamingFinderStrategy {

    default boolean support(FieldMetaInfo.MetaContext metaContext) {
        return metaContext.getFieldMetaInfo().getFactory().nameStrategy() == getClass();
    }

    /**
     * 该策略主要为了在方法调用时动态获取bean的名字,从而实现动态代理bean
     *
     * @param metaContext 方法参数等
     * @return bean名字
     */
    String findSupportType(FieldMetaInfo.MetaContext metaContext) throws Exception;

}
