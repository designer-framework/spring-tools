package org.designer.common.bean;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/18 23:10
 */
public enum RunStrategy {

    /**
     * 所有服务运行在相同的端口
     */
    ONE_PORT,
    /**
     * 每个服务运行在独立的端口
     */
    MANY_PORT;

}
