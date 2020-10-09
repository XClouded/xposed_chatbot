package com.taobao.monitor.impl.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface OnUsableVisibleListener<T> {
    public static final int STATUE_GONE = 0;
    public static final int STATUS_FULL_VISIBLE = 2;
    public static final int STATUS_VISIBLE = 1;
    public static final int USABLE_FULL_USABLE = 2;
    public static final int USABLE_PART_USABLE = 1;
    public static final int USABLE_UNUSABLE = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface UsableStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VisibleStatus {
    }

    void onRenderPercent(T t, float f, long j);

    void onRenderStart(T t, long j);

    void onUsableChanged(T t, int i, long j);

    void onVisibleChanged(T t, int i, long j);
}
