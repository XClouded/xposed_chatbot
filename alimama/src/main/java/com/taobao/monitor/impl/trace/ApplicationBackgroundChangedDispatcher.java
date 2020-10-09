package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.trace.AbsDispatcher;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ApplicationBackgroundChangedDispatcher extends AbsDispatcher<BackgroundChangedListener> {
    public static final int B2F = 0;
    public static final int F2B = 1;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

    public interface BackgroundChangedListener {
        void onChanged(int i, long j);
    }

    public void backgroundChanged(final int i, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<BackgroundChangedListener>() {
            public void callListener(BackgroundChangedListener backgroundChangedListener) {
                backgroundChangedListener.onChanged(i, j);
            }
        });
    }
}
