package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;

/* compiled from: U4Source */
final class x implements ValueCallback<l> {
    final /* synthetic */ w a;

    x(w wVar) {
        this.a = wVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        Log.d("SdkSetupTask", "ShareCoreSdcardSetupTask exception " + ((l) obj));
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_UPD_SC_INIT_EXCEPTION_PV);
        try {
            e.f(this.a.a.getContext().getApplicationContext());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
