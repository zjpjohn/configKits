package com.zjp.config.config;

import com.zjp.config.annotation.Config;
import com.zjp.config.core.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 */
public class AnnotationProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(AnnotationProcessor.class);

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        accessField(bean);
        accessMethod(bean);
        return bean;
    }

    /**
     * 处理字段
     *
     * @param targetObj
     */
    private void accessField(Object targetObj) {
        Field[] fields = targetObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            Config annotation = field.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    DataLoader.addConfigListener(annotation.value(), targetObj, field).listen();
                } catch (Exception e) {
                    log.error("listen field error:{}", e);
                }
            }
        }
    }

    /**
     * 处理方法
     *
     * @param targetObj
     */
    private void accessMethod(Object targetObj) {
        Method[] methods = targetObj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Config annotation = method.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    DataLoader.addConfigListener(annotation.value(), targetObj, method).listen();
                } catch (Exception e) {
                    log.error("listen method error:{}", e);
                }
            }
        }
    }
}
