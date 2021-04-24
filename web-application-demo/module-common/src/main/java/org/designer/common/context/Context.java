package org.designer.common.context;

import org.designer.common.bean.AppContext;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 1:05
 */
public interface Context {

    void startApp(AppContext appContext);

    int getConst();

}
