package com.zjp.config.core.impl;

import com.zjp.config.core.Configuration;
import com.zjp.config.core.PropertyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 * <p/>
 * 针对String类型的配置项
 */
public class StringConfiguration implements Configuration<String> {

    private static final Logger log = LoggerFactory.getLogger(StringConfiguration.class);

    //配置项的键
    private String key;

    //配置项的值
    private String value;

    //监听器集合
    private CopyOnWriteArrayList<PropertyListener> listeners = new CopyOnWriteArrayList<PropertyListener>();

    public StringConfiguration() {
    }

    public StringConfiguration(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 获取配置值的类型
     *
     * @return
     */
    public String getType() {
        return value.getClass().getSimpleName();
    }

    /**
     * 给field添加监听事件
     *
     * @param field
     * @param targetObj
     * @return
     */
    public PropertyListener addListener(Field field, Object targetObj) {
        FieldPropertyListener<String> listener = new FieldPropertyListener<String>(this, field, targetObj);
        listeners.add(listener);
        return listener;
    }

    /**
     * 给method添加监听事件
     *
     * @param method
     * @param targetObj
     * @return
     */
    public PropertyListener addListener(Method method, Object targetObj) {
        MethodPropertyListener<String> listener = new MethodPropertyListener<String>(method, targetObj, this);
        listeners.add(listener);
        return listener;
    }

    /**
     * 触发监听事件
     * 当配置改变时，立即出发事件
     */
    public void trigger() {
        for (PropertyListener listener : listeners) {
            listener.listen();
        }
    }
}
