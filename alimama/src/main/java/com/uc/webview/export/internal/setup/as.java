package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;

/* compiled from: U4Source */
final class as implements ValueCallback<l> {
    final /* synthetic */ ap a;

    as(ap apVar) {
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String a2 = ap.b;
        Log.d(a2, "exception " + ((l) obj));
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELAY_SEARE_CORE_FILE_EXCEPTION_PV);
        try {
            e.f(this.a.getContext().getApplicationContext());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
