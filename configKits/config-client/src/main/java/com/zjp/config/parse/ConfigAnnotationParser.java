package com.zjp.config.parse;

import com.zjp.config.config.AnnotationProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 */
public class ConfigAnnotationParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return AnnotationProcessor.class;
    }


    @Override
    protected String getBeanClassName(Element element) {
        return AnnotationProcessor.class.getName();
    }


    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);
    }


    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
