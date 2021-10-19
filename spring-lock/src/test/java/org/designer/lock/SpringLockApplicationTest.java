package org.designer.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 19:06
 */
@EnableAspectJAutoProxy()
@SpringBootApplication(scanBasePackages = "org.designer")
public class SpringLockApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(SpringLockApplicationTest.class, args);
    }

}
