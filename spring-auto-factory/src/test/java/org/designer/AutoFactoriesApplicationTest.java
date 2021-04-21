package org.designer;


import lombok.AllArgsConstructor;
import org.designer.di.annotation.EnabledAutoFactoryDi;
import org.designer.di.service.TestDiService;
import org.designer.di.service.impl.TestDiServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */

@EnabledAutoFactoryDi(enable = true)
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AutoFactoriesApplicationTest.AutoFactoriesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutoFactoriesApplicationTest {

    @Autowired
    private TestDiService testPay;

    @Autowired
    private BeanFactory beanFactory;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        String wx = testPay.doTest(new TestRequestModel("wx", BigDecimal.valueOf(666)));
        System.out.println(wx);

    }


    @AllArgsConstructor
    private static class TestRequestModel implements TestDiServiceImpl.TestRequest {

        private static final long serialVersionUID = 7987700219090159822L;

        private final String type;

        private final BigDecimal money;

        @Override
        public String getType() {
            return type;
        }

        @Override
        public BigDecimal getMoney() {
            return money;
        }

    }

    @EnableAutoConfiguration
    public static class AutoFactoriesApplication {
        public static void main(String[] args) {
            System.out.println("Hello World!");
        }
    }


}
