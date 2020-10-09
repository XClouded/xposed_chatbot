package com.taobao.weex.devtools.json.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonProperty {
    boolean required() default false;
}
