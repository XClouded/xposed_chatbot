package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.webkit.CookieSyncManager;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;

/* compiled from: U4Source */
public final class ay extends l {
    public final void run() {
        try {
            CookieSyncManager.createInstance((Context) getOption("CONTEXT"));
        } catch (RuntimeException unused) {
        }
        callback(UCCore.LEGACY_EVENT_SETUP);
        callback("load");
        SDKFactory.f(2);
        setLoadedUCM(new UCMRunningInfo(getContext(), (br) null, (ClassLoader) null, (ClassLoader) null, false, false, (UCMobileWebKit) null, 2, false, 0));
        callback("init");
        callback("switch");
        IWaStat.WaStat.stat(IWaStat.KEY_SYSTEM_SETUP_PV);
    }
}
