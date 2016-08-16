package com.zjp.config.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 */
public interface Configuration<T> {

    void setValue(T t);

    T getValue();

    /**
     * 获取配置值的类型
     *
     * @return
     */

    String getType();

    /**
     * 给field添加监听事件
     *
     * @param field
     * @param targetObj
     * @return
     */
    PropertyListener addListener(Field field, Object targetObj);

    /**
     * 给method添加监听事件
     *
     * @param method
     * @param targetObj
     * @return
     */
    PropertyListener addListener(Method method, Object targetObj);

    /**
     * 触发监听事件
     * 当配置改变时，立即出发事件
     */
    void trigger();
}
