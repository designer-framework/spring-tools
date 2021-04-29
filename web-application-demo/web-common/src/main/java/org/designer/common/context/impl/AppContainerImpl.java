package org.designer.common.context.impl;

import org.designer.common.context.AppContainer;
import org.designer.common.context.Context;

import java.util.Collections;
import java.util.Map;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.context
 * @Author: Designer
 * @CreateTime: 2021-04-24 22
 * @Description:
 */

public class AppContainerImpl implements AppContainer {

    private final Map<String, Context> contexts;

    public AppContainerImpl(Map<String, Context> contexts) {
        this.contexts = contexts;
    }

    @Override
    public Map<String, Context> getAppContexts() {
        return Collections.unmodifiableMap(contexts);
    }

    @Override
    public Object handler(String requestUrl) {
        return "null";
    }

}
