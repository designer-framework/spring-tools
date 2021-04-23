package org.designer.xml.handler;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.lang.Nullable;
import org.w3c.dom.Element;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 22:15
 */
public class CustomizeXmlSupportParse extends AbstractSingleBeanDefinitionParser {

    private final String className = "class-name";

    /**
     * xsd文件已经对参数有严格限制
     *
     * @param element element元素
     * @return
     */
    @Override
    @Nullable
    protected Class<?> getBeanClass(Element element) {
        String clazzName = element.getAttribute(className);
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("找不到类:" + clazzName, e);
        }

    }

    @Override
    @Nullable
    protected String getBeanClassName(Element element) {
        return element.getAttribute(className);
    }

    /**
     * 本例子通过构造器实例化bean
     *
     * @param element
     * @param parserContext
     * @param builder
     */
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String arg1 = element.getAttribute("arg1");
        String arg2 = element.getAttribute("arg2");
        builder.addConstructorArgValue(arg1);
        builder.addConstructorArgValue(arg2);
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

    }
}
