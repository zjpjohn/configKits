package com.zjp.config.annotation;

import java.lang.annotation.*;

/**
 * author:zjprevenge
 * time: 2016/8/1
 * copyright all reserved
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {
    String value() default "";
}
