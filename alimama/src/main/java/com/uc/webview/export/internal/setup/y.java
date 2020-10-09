package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class y implements ValueCallback<l> {
    final /* synthetic */ w a;

    y(w wVar) {
        this.a = wVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        Log.d("SdkSetupTask", "ShareCoreSdcardSetupTask success " + ((l) obj));
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_UPD_SC_INIT_SUCCESS_PV);
    }
}
