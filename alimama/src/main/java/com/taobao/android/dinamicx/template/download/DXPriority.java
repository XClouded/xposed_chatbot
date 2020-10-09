package com.taobao.android.dinamicx.template.download;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class DXPriority {
    public static final int HIGH = 0;
    public static final int LOW = 2;
    public static final int NORMAL = 1;

    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DXPriorityDef {
    }
}
