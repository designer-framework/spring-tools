package org.designer.lock.service;

import org.designer.lock.annotation.Key;
import org.designer.lock.annotation.Lock;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Designer
 * @date : 2021/10/19 23:19
 */
@Component
public class TestLockService {

    @Lock(key = {@Key(name = "#p0")})
    public void lock(Integer p0) {

    }

}
