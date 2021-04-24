package  org.designer.app.starter;

import lombok.extern.log4j.Log4j2;
import org.designer.app.context.ContextImpl;
import org.designer.common.bean.AppContext;
import org.designer.common.context.Context;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.starter
 * @Author: Designer
 * @CreateTime: 2021-04-24 18
 * @Description:
 */
@Log4j2
public class Starter {

    public static Context start(AppContext appContext) {
        Context context = new ContextImpl();
        context.startApp(appContext);
        return context;
    }


}
