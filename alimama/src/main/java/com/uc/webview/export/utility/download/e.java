package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: U4Source */
final class e implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ UpdateTask b;

    e(UpdateTask updateTask, ValueCallback valueCallback) {
        this.b = updateTask;
        this.a = valueCallback;
    }

    public final /* bridge */ /* synthetic */ void onReceiveValue(Object obj) {
        if (this.a != null) {
            this.a.onReceiveValue(this.b);
        }
    }
}
