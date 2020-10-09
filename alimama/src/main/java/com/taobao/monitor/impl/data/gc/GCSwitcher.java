package com.taobao.monitor.impl.data.gc;

import com.taobao.monitor.impl.trace.ApplicationBackgroundChangedDispatcher;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;

public class GCSwitcher implements ApplicationGCDispatcher.ApplicationGCListener, IGCSwitcher, ApplicationBackgroundChangedDispatcher.BackgroundChangedListener {
    private static final String TAG = "GCSwitcher";
    private volatile boolean mOpen = false;

    public void gc() {
        if (this.mOpen) {
            createGCDetector();
        }
    }

    public void open() {
        if (!this.mOpen) {
            this.mOpen = true;
            createGCDetector();
        }
    }

    public void close() {
        this.mOpen = false;
    }

    public void onChanged(int i, long j) {
        if (i == 0) {
            open();
        } else {
            close();
        }
    }

    private void createGCDetector() {
        new GCDetector();
    }
}
