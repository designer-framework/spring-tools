package org.designer.app.context;

import lombok.extern.log4j.Log4j2;
import org.designer.common.bean.AppContext;
import org.designer.common.context.Context;

/**
 * @description: 该类为测试类
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
public class ContextImpl implements Context {

    private static int CONST = 0;

    private final String THREAD_NAME = Thread.currentThread().getName();

    @Override
    public int getConst() {
        return ContextImpl.CONST;
    }

    @Override
    public void startApp(AppContext appContext) {
        log.info(THREAD_NAME + ": 启动App中!");
        log.info(THREAD_NAME + ": 常量值, " + CONST);
        log.info(THREAD_NAME + ": 启动App中!");
        log.info(THREAD_NAME + ": 常量值, " + ++CONST);
        log.info(THREAD_NAME + ": APP类加载器: " + getClass().getClassLoader());
        log.info(THREAD_NAME + ": APP加载完成");
    }
}
