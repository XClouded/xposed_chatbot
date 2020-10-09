package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.data.OnUsableVisibleListener;
import com.taobao.monitor.impl.trace.AbsDispatcher;

public class UsableVisibleDispatcher<T> extends AbsDispatcher<OnUsableVisibleListener> implements OnUsableVisibleListener {
    public void onVisibleChanged(Object obj, int i, long j) {
        final Object obj2 = obj;
        final int i2 = i;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<OnUsableVisibleListener>() {
            public void callListener(OnUsableVisibleListener onUsableVisibleListener) {
                onUsableVisibleListener.onVisibleChanged(obj2, i2, j2);
            }
        });
    }

    public void onUsableChanged(Object obj, int i, long j) {
        final Object obj2 = obj;
        final int i2 = i;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<OnUsableVisibleListener>() {
            public void callListener(OnUsableVisibleListener onUsableVisibleListener) {
                onUsableVisibleListener.onUsableChanged(obj2, i2, j2);
            }
        });
    }

    public void onRenderStart(final Object obj, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<OnUsableVisibleListener>() {
            public void callListener(OnUsableVisibleListener onUsableVisibleListener) {
                onUsableVisibleListener.onRenderStart(obj, j);
            }
        });
    }

    public void onRenderPercent(Object obj, float f, long j) {
        final Object obj2 = obj;
        final float f2 = f;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<OnUsableVisibleListener>() {
            public void callListener(OnUsableVisibleListener onUsableVisibleListener) {
                onUsableVisibleListener.onRenderPercent(obj2, f2, j2);
            }
        });
    }
}
