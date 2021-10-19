package org.designer.lock.test;

import org.designer.lock.SpringLockApplicationTest;
import org.designer.lock.interceptor.LockInterceptor;
import org.designer.lock.service.TestLockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringLockApplicationTest.class)
public class LockInterceptorTest {

    @Autowired
    private LockInterceptor lockInterceptor;

    @Autowired
    private TestLockService testLockService;

    @Test
    public void lockInterceptor() {
        testLockService.lock(1);
    }


}
