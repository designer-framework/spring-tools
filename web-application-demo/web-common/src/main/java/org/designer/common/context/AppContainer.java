package org.designer.common.context;

import java.util.Map;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.context
 * @Author: Designer
 * @CreateTime: 2021-04-24 22
 * @Description:
 */

public interface AppContainer extends RequestHandler {

    Map<String, Context> getAppContexts();

}
