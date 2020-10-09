package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class at implements ValueCallback<l> {
    final /* synthetic */ ap a;

    at(ap apVar) {
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String a2 = ap.b;
        Log.d(a2, "success " + ((l) obj));
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELAY_SEARE_CORE_FILE_SUCCESS_PV);
    }
}
