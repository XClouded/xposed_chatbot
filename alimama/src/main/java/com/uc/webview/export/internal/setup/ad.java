package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class ad implements ValueCallback<l> {
    final /* synthetic */ o a;

    ad(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d("SdkSetupTask", "mSwitchCB " + lVar);
        if (lVar.getLoadedUCM() != null) {
            o.a(this.a, lVar.getLoadedUCM());
            this.a.f();
            ValueCallback callback = this.a.getCallback("switch");
            if (callback != null) {
                callback.onReceiveValue(this.a);
            }
        }
    }
}
