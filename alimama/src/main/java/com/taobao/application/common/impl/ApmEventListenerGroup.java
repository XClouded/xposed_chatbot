package com.taobao.application.common.impl;

import com.taobao.application.common.IApmEventListener;
import java.util.ArrayList;
import java.util.Iterator;

public class ApmEventListenerGroup implements IApmEventListener, IListenerGroup<IApmEventListener> {
    /* access modifiers changed from: private */
    public final ArrayList<IApmEventListener> listeners = new ArrayList<>();

    public void onEvent(final int i) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApmEventListenerGroup.this.listeners.iterator();
                while (it.hasNext()) {
                    ((IApmEventListener) it.next()).onEvent(i);
                }
            }
        });
    }

    public void addListener(final IApmEventListener iApmEventListener) {
        if (iApmEventListener != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    if (!ApmEventListenerGroup.this.listeners.contains(iApmEventListener)) {
                        ApmEventListenerGroup.this.listeners.add(iApmEventListener);
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    public void removeListener(final IApmEventListener iApmEventListener) {
        if (iApmEventListener != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    ApmEventListenerGroup.this.listeners.remove(iApmEventListener);
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    private void innerRunnable(Runnable runnable) {
        ApmImpl.instance().secHandler(runnable);
    }
}
