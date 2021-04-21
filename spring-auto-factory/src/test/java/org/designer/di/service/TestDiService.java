package org.designer.di.service;

import org.designer.di.service.impl.TestDiServiceImpl;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/16 10:00
 */
public interface TestDiService {

    /**
     * 测试代理对象
     * 1.可以自行实现 NamingStrategy 接口来实现如何获取bean
     *
     * @param type 支付类型接口
     * @return
     */
    String doTest(TestDiServiceImpl.TestRequest type);

}
