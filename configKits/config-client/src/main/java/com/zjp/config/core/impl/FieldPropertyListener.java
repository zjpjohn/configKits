package com.zjp.config.core.impl;

import com.zjp.config.core.Configuration;
import com.zjp.config.core.PropertyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 * <p/>
 * 监听对象的值变化
 */
public class FieldPropertyListener<T> implements PropertyListener {

    private static final Logger log = LoggerFactory.getLogger(FieldPropertyListener.class);

    private Configuration<T> configuration;

    private Field field;

    private Object targetObj;

    public FieldPropertyListener() {
    }

    public FieldPropertyListener(Configuration<T> configuration, Field field, Object targetObj) {
        this.configuration = configuration;
        this.field = field;
        this.targetObj = targetObj;
    }

    public Configuration<T> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration<T> configuration) {
        this.configuration = configuration;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
    }

    /**
     * 配置
     */
    public void listen() {
        log.info("listen field:{}...", field.getName());
        String type = field.getType().getSimpleName();
        if (type != configuration.getType()) {
            return;
        }
        field.setAccessible(true);
        try {
            field.set(targetObj, configuration.getValue());
        } catch (IllegalAccessException e) {
            log.error("set field value:{}", e);
        }
    }
}
