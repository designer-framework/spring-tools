package org.designer.di.service;

import org.designer.di.service.impl.TestDiServiceImpl;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 16:19
 */
public interface TestPay {

    /**
     * 调用支付接口
     *
     * @param s
     * @return
     */
    String doPay(TestDiServiceImpl.TestRequest s);

    public boolean match(String type);

}
