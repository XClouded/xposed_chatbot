package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;

/* compiled from: U4Source */
final class aa implements ValueCallback<l> {
    final ValueCallback a = this.b.getCallback(UCCore.EVENT_UPDATE_PROGRESS);
    final /* synthetic */ o b;

    aa(o oVar) {
        this.b = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.b.mPercent = ((l) obj).getPercent();
        if (this.a != null) {
            this.a.onReceiveValue(this.b);
        }
    }
}
