package org.designer.common.context;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
public class ContextImpl implements Context {

    private static final int JAR_VERSION = 2;

    public static void main(String[] args) {
        ContextImpl context = new ContextImpl();
        context.refresh(args);
    }

    @Override
    public void refresh(Object obj) {
        log.info("JAR_VERSION: " + JAR_VERSION);
        log.info("args: " + JSON.toJSONString(obj));
    }

}
