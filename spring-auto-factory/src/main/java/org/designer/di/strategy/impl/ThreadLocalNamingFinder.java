package org.designer.di.strategy.impl;

import org.designer.di.FieldMetaInfo;
import org.designer.di.strategy.AbstractNamingFinder;
import org.designer.di.strategy.holder.NamingHolder;

/**
 * @description: 默认策略实现
 * @author: Designer
 * @date : 2021/4/15 19:29
 */
public class ThreadLocalNamingFinder extends AbstractNamingFinder {

    private static final NamingHolder HOLDER = new NamingHolder();

    public static void set(String beanName) {
        HOLDER.set(beanName);
    }

    public static void remove() {
        HOLDER.remove();
    }

    @Override
    public String findSupportType(FieldMetaInfo.MetaContext metaContext) {
        return HOLDER.get();
    }

    @Override
    protected String doGetName(Object[] args) {
        return HOLDER.get();
    }

    @Override
    public boolean support(FieldMetaInfo.MetaContext fieldMetaInfo) {
        return HOLDER.get() != null;
    }

}

