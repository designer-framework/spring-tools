package org.designer.common.context;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.context
 * @Author: Designer
 * @CreateTime: 2021-04-24 22
 * @Description:
 */

public interface RequestHandler {

    Object handler(String requestUrl);

}
