package org.designer.di.service.impl;

import org.designer.di.annotation.Factory;
import org.designer.di.annotation.MatchInvoke;
import org.designer.di.service.TestDiService;
import org.designer.di.service.TestPay;
import org.designer.di.strategy.DefaultType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 16:35
 */
@Service
public class TestDiServiceImpl implements TestDiService {

    //@Factory(matchInvoke = @MatchInvoke(enable = true))
    @Factory(matchInvoke = @MatchInvoke(enable = true))
    private TestPay testPay;

    @Override
    public String doTest(TestRequest type) {
        return testPay.doPay(type);
    }

    public interface TestRequest extends DefaultType {

        @Override
        String getType();

        BigDecimal getMoney();

        default String getPayInfo() {
            return getType() + "支付了" + getMoney() + "元";
        }

    }

}
