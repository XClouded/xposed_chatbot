package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.trace.AbsDispatcher;

public class ApplicationGCDispatcher extends AbsDispatcher<ApplicationGCListener> {

    public interface ApplicationGCListener {
        void gc();
    }

    public void dispatchGC() {
        foreach(new AbsDispatcher.ListenerCaller<ApplicationGCListener>() {
            public void callListener(ApplicationGCListener applicationGCListener) {
                applicationGCListener.gc();
            }
        });
    }
}
