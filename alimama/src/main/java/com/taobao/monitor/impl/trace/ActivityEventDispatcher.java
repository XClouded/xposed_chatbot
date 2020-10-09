package com.taobao.monitor.impl.trace;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.taobao.monitor.impl.trace.AbsDispatcher;

public class ActivityEventDispatcher extends AbsDispatcher<OnEventListener> {

    public interface OnEventListener {
        void onKey(Activity activity, KeyEvent keyEvent, long j);

        void onTouch(Activity activity, MotionEvent motionEvent, long j);
    }

    public void onKey(Activity activity, KeyEvent keyEvent, long j) {
        final Activity activity2 = activity;
        final KeyEvent keyEvent2 = keyEvent;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<OnEventListener>() {
            public void callListener(OnEventListener onEventListener) {
                onEventListener.onKey(activity2, keyEvent2, j2);
            }
        });
    }

    public void onTouch(Activity activity, MotionEvent motionEvent, long j) {
        final Activity activity2 = activity;
        final MotionEvent motionEvent2 = motionEvent;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<OnEventListener>() {
            public void callListener(OnEventListener onEventListener) {
                onEventListener.onTouch(activity2, motionEvent2, j2);
            }
        });
    }
}
