package org.designer.common.context;

import org.designer.common.bean.ContextInfo;
import org.designer.common.classload.AppClassloader;
import org.designer.common.utils.AppUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:45
 */
public class AppsContext {

    private final Map<String, ContextInfo> contexts;

    public AppsContext(Map<String, ContextInfo> contexts) {
        if (!(contexts instanceof ConcurrentHashMap)) {
            contexts = new ConcurrentHashMap<>(contexts);
        }
        this.contexts = contexts;
    }

    public void loadApps() {
        contexts.forEach((s, contextInfo) -> {
            try {
                AppClassloader appClassloader = new AppClassloader(new File(contextInfo.getAppPath()));
                AppUtils.reflectInvokeApp(contextInfo.getMain(), appClassloader, contextInfo);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
