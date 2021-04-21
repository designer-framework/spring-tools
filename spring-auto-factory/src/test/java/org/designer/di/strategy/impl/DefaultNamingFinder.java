package org.designer.di.strategy.impl;

import org.designer.di.FieldMetaInfo;
import org.designer.di.service.impl.TestDiServiceImpl;
import org.designer.di.strategy.AbstractNamingFinder;
import org.designer.di.strategy.DefaultType;

/**
 * @description: 默认策略实现
 * @author: Designer
 * @date : 2021/4/15 19:29
 */
public class DefaultNamingFinder extends AbstractNamingFinder {

    @Override
    public String findSupportType(FieldMetaInfo.MetaContext metaContext) {
        return doGetName(metaContext.getArgs());
    }

    @Override
    protected String doGetName(Object[] args) {
        return ((TestDiServiceImpl.TestRequest) args[0]).getType();
    }

    @Override
    public boolean support(FieldMetaInfo.MetaContext fieldMetaInfo) {
        return fieldMetaInfo.getArgs() != null && fieldMetaInfo.getArgs().length > 0 && fieldMetaInfo.getArgs()[0] instanceof DefaultType;
    }

}

