package com.zjp.config.handler;


import com.zjp.config.parse.ConfigAnnotationParser;
import com.zjp.config.parse.ConfigPlaceholderParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * author:zjprevenge
 * time: 2016/8/1
 * copyright all reserved
 */
public class ConfigNamespaceHandler extends NamespaceHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ConfigNamespaceHandler.class);

    public void init() {
        log.info("init config namespace...");
        //连接配置中心
        registerBeanDefinitionParser("config", new ConfigPlaceholderParser());
        //开启动态配置
        registerBeanDefinitionParser("annotation", new ConfigAnnotationParser());
    }
}
