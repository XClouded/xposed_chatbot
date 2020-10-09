package com.taobao.application.common.impl;

import com.taobao.application.common.IAppLaunchListener;
import java.util.ArrayList;
import java.util.List;

class AppLaunchListenerGroup implements IAppLaunchListener, IListenerGroup<IAppLaunchListener> {
    /* access modifiers changed from: private */
    public final List<IAppLaunchListener> listeners = new ArrayList(2);

    AppLaunchListenerGroup() {
    }

    public void addListener(final IAppLaunchListener iAppLaunchListener) {
        if (iAppLaunchListener != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    if (!AppLaunchListenerGroup.this.listeners.contains(iAppLaunchListener)) {
                        AppLaunchListenerGroup.this.listeners.add(iAppLaunchListener);
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    public void removeListener(final IAppLaunchListener iAppLaunchListener) {
        if (iAppLaunchListener != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    AppLaunchListenerGroup.this.listeners.remove(iAppLaunchListener);
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    public void onLaunchChanged(final int i, final int i2) {
        innerRunnable(new Runnable() {
            public void run() {
                for (IAppLaunchListener onLaunchChanged : AppLaunchListenerGroup.this.listeners) {
                    onLaunchChanged.onLaunchChanged(i, i2);
                }
            }
        });
    }

    private void innerRunnable(Runnable runnable) {
        ApmImpl.instance().secHandler(runnable);
    }
}
