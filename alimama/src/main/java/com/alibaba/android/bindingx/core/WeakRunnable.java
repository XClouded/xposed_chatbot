package com.alibaba.android.bindingx.core;

import androidx.annotation.NonNull;
import java.lang.ref.WeakReference;

public class WeakRunnable implements Runnable {
    private final WeakReference<Runnable> mDelegateRunnable;

    public WeakRunnable(@NonNull Runnable runnable) {
        this.mDelegateRunnable = new WeakReference<>(runnable);
    }

    public void run() {
        Runnable runnable = (Runnable) this.mDelegateRunnable.get();
        if (runnable != null) {
            try {
                runnable.run();
            } catch (Throwable th) {
                LogProxy.e(th.getMessage());
            }
        }
    }
}
