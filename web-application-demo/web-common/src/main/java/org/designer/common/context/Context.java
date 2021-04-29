package org.designer.common.context;

import org.designer.common.utils.MethodInvoke;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 1:05
 */
public interface Context {

    boolean support(String appName);

    MethodInvoke getAppRequestMapping(String path);

}
