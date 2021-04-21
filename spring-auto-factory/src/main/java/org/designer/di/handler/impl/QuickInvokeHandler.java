package org.designer.di.handler.impl;

import org.designer.di.FieldMetaInfo;
import org.designer.di.enums.InvokeHandlerType;
import org.designer.di.handler.AbstractInvokeHandler;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/16 16:28
 */
public class QuickInvokeHandler extends AbstractInvokeHandler {

    @Override
    protected Object getBeanByType(String supportType, FieldMetaInfo.MetaContext metaContext) throws Exception {
        Field field = metaContext.getField();
        Map<String, ?> beansOfType = beanFactory.getBeansOfType(field.getType());
        Object bean = beansOfType.get(supportType);
        if (bean == null) {
            throw new BeanCreationException("未找到合适的bean:" + metaContext);
        }
        return bean;
    }

    @Override
    public boolean support(FieldMetaInfo fieldMetaInfo) {
        return InvokeHandlerType.MATCH_INVOKE == fieldMetaInfo.getFactory().type() && !fieldMetaInfo.getFactory().matchInvoke().enable();
    }

}
