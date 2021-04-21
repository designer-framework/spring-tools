package org.designer.di.handler.impl;

import org.designer.di.FieldMetaInfo;
import org.designer.di.enums.InvokeHandlerType;
import org.designer.di.handler.AbstractInvokeHandler;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @description: 在具体的业务类中调用匹配方法, 返回true则表示匹配成功
 * @author: Designer
 * @date : 2021/4/16 16:28
 */
public class MatchInvokeHandler extends AbstractInvokeHandler {

    @Override
    protected Object getBeanByType(String supportType, FieldMetaInfo.MetaContext metaContext) throws Exception {
        FieldMetaInfo fieldMetaInfo = metaContext.getFieldMetaInfo();
        Field field = fieldMetaInfo.getField();
        Map<String, ?> beansOfType = beanFactory.getBeansOfType(field.getType());
        Set<? extends Map.Entry<String, ?>> entries = beansOfType.entrySet();
        for (Map.Entry<String, ?> entry : entries) {
            Object result = fieldMetaInfo.getValidMethod().invoke(entry.getValue(), parserType(supportType));
            if (result instanceof Boolean && (Boolean) result) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("xx");
    }

    private Object parserType(String supportType) {
        return supportType;
    }


    @Override
    public boolean support(FieldMetaInfo fieldMetaInfo) {
        return InvokeHandlerType.MATCH_INVOKE == fieldMetaInfo.getFactory().type() && fieldMetaInfo.getFactory().matchInvoke().enable();
    }

}
