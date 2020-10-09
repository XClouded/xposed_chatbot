package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;

/* compiled from: U4Source */
final class t implements ValueCallback<f> {
    final /* synthetic */ o a;

    t(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ValueCallback callback = this.a.getCallback(UCCore.EVENT_DELETE_FILE_FINISH);
        if (callback != null) {
            callback.onReceiveValue(this.a);
        }
    }
}
