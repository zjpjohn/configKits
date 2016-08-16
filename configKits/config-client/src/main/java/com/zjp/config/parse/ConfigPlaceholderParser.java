package com.zjp.config.parse;

import com.zjp.config.config.ConfigPlacehodlerProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 */
public class ConfigPlaceholderParser extends AbstractSingleBeanDefinitionParser {

    private static final String SERVRE_URL = "zkUrl";
    private static final String ENVIRONMENT = "env";
    private static final String APP_NAME = "appName";
    private static final String CHARSET = "charset";


    @Override
    protected Class<?> getBeanClass(Element element) {
        return ConfigPlacehodlerProcessor.class;
    }


    @Override
    protected String getBeanClassName(Element element) {
        return ConfigPlacehodlerProcessor.class.getName();
    }


    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String zkUrl = element.getAttribute(SERVRE_URL);
        String env = element.getAttribute(ENVIRONMENT);
        String appName = element.getAttribute(APP_NAME);
        String charset = element.getAttribute(CHARSET);
        if (StringUtils.isBlank(zkUrl)) {
            throw new RuntimeException(String.format("element[%s],attribute[%s] required", element.getLocalName(), SERVRE_URL));
        }
        if (StringUtils.isBlank(env)) {
            throw new RuntimeException(String.format("element[%s],attribute[%s] required", element.getLocalName(), ENVIRONMENT));
        }
        if (StringUtils.isBlank(appName)) {
            throw new RuntimeException(String.format("element[%s],attribute[%s] required", element.getLocalName(), APP_NAME));
        }
        //将配置参数传递给ConfigPlacehodlerProcessor
        builder.addPropertyValue(SERVRE_URL, zkUrl);
        builder.addPropertyValue(ENVIRONMENT, env);
        builder.addPropertyValue(APP_NAME, appName);
        builder.addPropertyValue(CHARSET, charset);
    }


    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
