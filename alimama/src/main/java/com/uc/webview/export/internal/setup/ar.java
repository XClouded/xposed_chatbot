package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class ar implements ValueCallback<l> {
    final /* synthetic */ ap a;

    ar(ap apVar) {
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        Log.d(ap.b, "setup callback.");
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELAY_SEARE_CORE_FILE_SETUP_PV);
        ((l) obj).stop();
    }
}
