package org.designer.common.web.util;

import org.designer.common.context.AppContext;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 6:53
 */
public interface SelectorHandler {

    //private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    void handler(SelectionKey selectionKey, Selector selector) throws Exception;

    void putAppContext(String appName, AppContext appContext);


}
