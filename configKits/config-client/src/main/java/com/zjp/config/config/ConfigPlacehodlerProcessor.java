package com.zjp.config.config;

import com.zjp.config.core.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 */
public class ConfigPlacehodlerProcessor extends PropertyPlaceholderConfigurer implements DisposableBean {


    private static final Logger log = LoggerFactory.getLogger(ConfigPlacehodlerProcessor.class);

    private ConfigClient configClient = new ConfigClient();

    public void setZkUrl(String zkUrl) {
        configClient.setZkUrl(zkUrl);
    }

    public void setEvn(String evn) {
        configClient.setEvn(evn);
    }

    public void setAppName(String appName) {
        configClient.setAppName(appName);
    }

    public void setCharset(String charset) {
        configClient.setCharset(charset);
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        log.info("load properties from zk:{}", configClient.getZkUrl());
        //初始化数据加载中心
        DataLoader.init(configClient.getZkUrl(),
                configClient.getAppName(),
                configClient.getEvn(),
                configClient.getCharset());
    }

    private class ConfigClient {
        private String zkUrl;
        private String evn;
        private String appName;
        private String charset;

        public String getZkUrl() {
            return zkUrl;
        }

        public void setZkUrl(String zkUrl) {
            this.zkUrl = zkUrl;
        }

        public String getEvn() {
            return evn;
        }

        public void setEvn(String evn) {
            this.evn = evn;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }
    }


    public void destroy() throws Exception {
        DataLoader.close();
    }

}
