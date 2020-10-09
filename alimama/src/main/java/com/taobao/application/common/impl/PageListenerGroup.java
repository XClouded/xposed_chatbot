package com.taobao.application.common.impl;

import com.taobao.application.common.IPageListener;
import java.util.ArrayList;
import java.util.Iterator;

class PageListenerGroup implements IPageListener, IListenerGroup<IPageListener> {
    /* access modifiers changed from: private */
    public ArrayList<IPageListener> listeners = new ArrayList<>();

    PageListenerGroup() {
    }

    public void onPageChanged(String str, int i, long j) {
        final String str2 = str;
        final int i2 = i;
        final long j2 = j;
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = PageListenerGroup.this.listeners.iterator();
                while (it.hasNext()) {
                    ((IPageListener) it.next()).onPageChanged(str2, i2, j2);
                }
            }
        });
    }

    public void addListener(final IPageListener iPageListener) {
        if (iPageListener != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    if (!PageListenerGroup.this.listeners.contains(iPageListener)) {
                        PageListenerGroup.this.listeners.add(iPageListener);
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    public void removeListener(final IPageListener iPageListener) {
        if (iPageListener != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    PageListenerGroup.this.listeners.remove(iPageListener);
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
