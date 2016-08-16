package com.zjp.config.core.impl;

import com.zjp.config.core.Configuration;
import com.zjp.config.core.PropertyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 * <p/>
 * 针对Map类型的配置项
 */
public class MapConfiguration implements Configuration<Map<String, String>> {

    private static final Logger log = LoggerFactory.getLogger(MapConfiguration.class);

    //配置项的键
    private String key;

    //配置项的值
    private Map<String, String> value;

    //监听器集合
    private CopyOnWriteArrayList<PropertyListener> listeners = new CopyOnWriteArrayList<PropertyListener>();

    public MapConfiguration() {
    }

    public MapConfiguration(String key, Map<String, String> value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

    public Map<String, String> getValue() {
        return this.value;
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
        FieldPropertyListener<Map<String, String>> listener = new FieldPropertyListener<Map<String, String>>(this, field, targetObj);
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
        MethodPropertyListener<Map<String, String>> listener = new MethodPropertyListener<Map<String, String>>(method, targetObj, this);
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
