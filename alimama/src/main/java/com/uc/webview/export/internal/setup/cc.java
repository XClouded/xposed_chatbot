package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class cc implements ValueCallback<UpdateTask> {
    final ValueCallback a = this.b.getCallback(UCCore.EVENT_DOWNLOAD_FILE_DELETE);
    final /* synthetic */ bw b;

    cc(bw bwVar) {
        this.b = bwVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String unused = this.b.b = ((UpdateTask) obj).getFilePath();
        if (this.a != null) {
            this.a.onReceiveValue(this.b);
        }
    }
}
