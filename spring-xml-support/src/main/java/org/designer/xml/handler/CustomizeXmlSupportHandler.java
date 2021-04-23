package org.designer.xml.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 22:15
 */
public class CustomizeXmlSupportHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("instance", new CustomizeXmlSupportParse());
    }


}
