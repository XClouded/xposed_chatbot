package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class n implements ValueCallback<UpdateTask> {
    n() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        IWaStat.WaStat.stat(IWaStat.VIDEO_UNZIP_SUCCESS);
    }
}
