package org.designer.di.handler.impl;

import org.designer.di.FieldMetaInfo;
import org.designer.di.handler.InvokeHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 15:25
 */
public class HandlerContext {

    @Autowired
    private Map<String, InvokeHandler> invokeHandler;

    public Optional<InvokeHandler> getHandler(FieldMetaInfo fieldMetaInfo) {
        return invokeHandler.values().stream().filter(handler -> handler.support(fieldMetaInfo)).findAny();
    }

}
