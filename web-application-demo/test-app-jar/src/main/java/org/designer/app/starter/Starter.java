package org.designer.app.starter;

import lombok.extern.log4j.Log4j2;
import org.designer.app.context.AppContextInfo;
import org.designer.common.bean.App;
import org.designer.common.context.AppContext;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.starter
 * @Author: Designer
 * @CreateTime: 2021-04-24 18
 * @Description:
 */
@Log4j2
public class Starter {

    public static AppContext start(App app) {
        return new AppContextInfo(app).startApp();
    }


}
