package org.designer.common.context;

import org.designer.common.bean.App;
import org.designer.common.utils.MethodInvoke;

import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 1:05
 */
public interface AppContext {

    /**
     * 每个APP都有一个AppName, 通过对应的AppName找到对应的Context
     *
     * @param appName
     * @return
     */
    boolean support(String appName);

    /**
     * 通过请求路径找方法
     *
     * @param path
     * @return
     */
    MethodInvoke getAppRequestMapping(String path);

    /**
     * 收集所有类中RequestMapping的URL
     *
     * @return
     */
    List<String> getRequestMappingUrls();

    /**
     * @return APP基本信息
     */
    App getAppInfo();

}
