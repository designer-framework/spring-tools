package org.designer.common.bean;

import java.io.Serializable;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.bean
 * @Author: Designer
 * @CreateTime: 2021-04-24 18
 * @Description:
 */

public interface App extends Serializable {

    String getAppPath();

    String getMain();

    String getVersion();

    String getAppName();

    int getPort();
}
