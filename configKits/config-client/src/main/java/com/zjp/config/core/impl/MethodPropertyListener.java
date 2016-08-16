package com.zjp.config.core.impl;

import com.zjp.config.core.Configuration;
import com.zjp.config.core.PropertyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 * <p/>
 * 监听方法的值变化
 */
public class MethodPropertyListener<T> implements PropertyListener {

    private static final Logger log = LoggerFactory.getLogger(MethodPropertyListener.class);

    //监听对象的method
    private Method method;

    //监听的对象
    private Object targetObj;

    //配置数据
    private Configuration<T> configuration;

    public MethodPropertyListener() {
    }

    public MethodPropertyListener(Method method,
                                  Object targetObj,
                                  Configuration<T> configuration) {
        this.method = method;
        this.targetObj = targetObj;
        this.configuration = configuration;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
    }

    public Configuration<T> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration<T> configuration) {
        this.configuration = configuration;
    }

    /**
     * 配置
     */
    public void listen() {
        Class<?>[] types = method.getParameterTypes();
        //参数个数判断
        if (types.length != 1) {
            throw new RuntimeException("方法:" + method.getName() + "参数的个数必须为1");
        }
        //参数类型判断
        if (configuration.getType() != types[0].getSimpleName()) {
            throw new RuntimeException("方法:" + method.getName() + "参数类型:" + types[0].getName()
                    + "和配置项的类型:" + configuration.getType() + "必须相同");
        }
        method.setAccessible(true);
        try {
            method.invoke(targetObj, configuration.getValue());
        } catch (IllegalAccessException e) {
            log.error("动态配置对象的值错误:{}", e);
        } catch (InvocationTargetException e) {
            log.error("动态配置对象的值错误:{}", e);
        }
    }
}
