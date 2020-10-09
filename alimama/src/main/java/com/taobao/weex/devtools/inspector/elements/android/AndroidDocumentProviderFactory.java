package com.taobao.weex.devtools.inspector.elements.android;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import com.taobao.weex.devtools.common.ThreadBound;
import com.taobao.weex.devtools.common.UncheckedCallable;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.common.android.HandlerUtil;
import com.taobao.weex.devtools.inspector.elements.DocumentProvider;
import com.taobao.weex.devtools.inspector.elements.DocumentProviderFactory;

public final class AndroidDocumentProviderFactory implements DocumentProviderFactory, ThreadBound {
    private final Application mApplication;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public AndroidDocumentProviderFactory(Application application) {
        this.mApplication = (Application) Util.throwIfNull(application);
    }

    public DocumentProvider create() {
        return new AndroidDocumentProvider(this.mApplication, this);
    }

    public boolean checkThreadAccess() {
        return HandlerUtil.checkThreadAccess(this.mHandler);
    }

    public void verifyThreadAccess() {
        HandlerUtil.verifyThreadAccess(this.mHandler);
    }

    public <V> V postAndWait(UncheckedCallable<V> uncheckedCallable) {
        return HandlerUtil.postAndWait(this.mHandler, uncheckedCallable);
    }

    public void postAndWait(Runnable runnable) {
        HandlerUtil.postAndWait(this.mHandler, runnable);
    }

    public void postDelayed(Runnable runnable, long j) {
        if (!this.mHandler.postDelayed(runnable, j)) {
            throw new RuntimeException("Handler.postDelayed() returned false");
        }
    }

    public void removeCallbacks(Runnable runnable) {
        this.mHandler.removeCallbacks(runnable);
    }
}
