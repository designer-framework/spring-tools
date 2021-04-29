package org.designer.starter.run.main;


import org.designer.common.bean.App;
import org.designer.common.utils.AppUtils;
import org.designer.common.utils.StarterUtils;
import org.designer.module.bean.AppInfo;
import org.designer.service.AppContextLoadUtil;

import java.util.List;
import java.util.Map;

/**
 * @Project: spring-tools
 * @Package: org.designer
 * @Author: Designer
 * @CreateTime: 2021-04-24 20
 * @Description:
 */

public class AppMain {

    public static void main(String[] args) {
        List<AppInfo> apps = AppUtils.getApps(AppInfo.class);
        Map<String, App> contexts = StarterUtils.listToMap(apps);
        new AppContextLoadUtil().loadApps(contexts);
    }

}
