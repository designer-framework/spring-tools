package org.designer.xml.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @description: 该类主要作用是初始化xml标签对应的解析类
 * @author: Designer
 * @date : 2021/4/23 22:15
 */
public class CustomizeXmlSupportHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("instance", new CustomizeXmlSupportParse());
    }


}
