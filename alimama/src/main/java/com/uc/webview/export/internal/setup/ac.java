package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;

/* compiled from: U4Source */
final class ac implements ValueCallback<l> {
    final ValueCallback a = this.b.getCallback(UCCore.EVENT_DOWNLOAD_EXCEPTION);
    final /* synthetic */ o b;

    ac(o oVar) {
        this.b = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        if (this.a != null) {
            if (lVar.getExtraException() != null) {
                this.b.setExtraException(lVar.getExtraException());
            }
            this.a.onReceiveValue(this.b);
        }
    }
}
