package org.designer.di.handler;

import org.designer.di.FieldMetaInfo;

import java.lang.reflect.InvocationHandler;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/16 16:16
 */
public interface InvokeHandler {

    boolean support(FieldMetaInfo fieldMetaInfo);

    InvocationHandler getInvocationHandler(FieldMetaInfo fieldMetaInfo);

}
