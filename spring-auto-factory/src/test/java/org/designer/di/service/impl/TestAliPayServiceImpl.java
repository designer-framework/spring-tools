package org.designer.di.service.impl;

import org.designer.di.service.TestPay;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 16:19
 */
@Component(value = "ali")
public class TestAliPayServiceImpl implements TestPay {

    @Override
    public String doPay(TestDiServiceImpl.TestRequest s) {
        return s.getPayInfo();
    }

    @Override
    public boolean match(String type) {
        return "ali".equals(type);
    }

}
