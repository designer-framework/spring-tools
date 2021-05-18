package org.designer.app.controller;

import lombok.extern.log4j.Log4j2;
import org.designer.common.annotation.Controller;
import org.designer.common.annotation.RequestMapping;

/**
 * @description: 该类为测试类
 * @author: Designer
 * @date : 2021/4/24 1:17
 */
@Log4j2
@Controller
public class TestController {


    @RequestMapping("/getClassLoader")
    public String getClassLoader() {
        return getClass().getClassLoader().toString();
    }


}
