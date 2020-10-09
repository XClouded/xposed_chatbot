package com.alibaba.taffy.bus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    String filter() default "";

    String group() default "";

    int priority() default 0;

    int status() default 0;

    int thread() default 2;
}
