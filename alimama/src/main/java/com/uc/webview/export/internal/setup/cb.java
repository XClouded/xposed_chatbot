package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class cb implements ValueCallback<UpdateTask> {
    final ValueCallback a = this.b.getCallback(UCCore.EVENT_UPDATE_PROGRESS);
    final /* synthetic */ bw b;

    cb(bw bwVar) {
        this.b = bwVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.b.mPercent = ((UpdateTask) obj).getPercent();
        if (this.a != null) {
            this.a.onReceiveValue(this.b);
        }
    }
}
