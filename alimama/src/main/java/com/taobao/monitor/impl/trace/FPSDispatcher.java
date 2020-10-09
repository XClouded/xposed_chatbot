package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.trace.AbsDispatcher;

public class FPSDispatcher extends AbsDispatcher<FPSListener> {

    public interface FPSListener {
        void fps(int i);

        void jank(int i);
    }

    public void fps(final int i) {
        foreach(new AbsDispatcher.ListenerCaller<FPSListener>() {
            public void callListener(FPSListener fPSListener) {
                fPSListener.fps(i);
            }
        });
    }

    public void jank(final int i) {
        foreach(new AbsDispatcher.ListenerCaller<FPSListener>() {
            public void callListener(FPSListener fPSListener) {
                fPSListener.jank(i);
            }
        });
    }
}
