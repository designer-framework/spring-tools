package org.designer.xml;

import org.designer.xml.bean.XmlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 22:45
 */
@RunWith(value = JUnit4.class)
public class Main {

    private final static String beanXml = "classpath:customize-xml-support.xml";

    @Autowired
    private XmlBean xmlBean;

    @Test
    public void testCustomizeXmlSupport() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(beanXml);
        XmlBean bean = classPathXmlApplicationContext.getBean(XmlBean.class);
        System.out.println(bean);
    }

}
