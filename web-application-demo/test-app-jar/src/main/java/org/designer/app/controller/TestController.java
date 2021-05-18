package org.designer.app.controller;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.designer.common.annotation.Controller;
import org.designer.common.annotation.RequestMapping;
import org.springframework.core.io.ClassPathResource;

/**
 * @description: 该类为测试类
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
@Controller
public class TestController {

    @SneakyThrows
    @RequestMapping("/")
    public String index() {
        return new ClassPathResource("", getClass()).getURL().getPath() + getClass().getSimpleName() + ".class";
    }

    @RequestMapping("/getClassLoader")
    public String getClassLoader() {
        return getClass().getClassLoader().toString();
    }

}
