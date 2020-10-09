package com.uc.webview.export.extension;

import android.content.Context;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;
import java.util.Map;

/* compiled from: U4Source */
final class l implements ValueCallback<UpdateTask> {
    final /* synthetic */ Context a;
    final /* synthetic */ Map b;

    l(Context context, Map map) {
        this.a = context;
        this.b = map;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        IWaStat.WaStat.stat(IWaStat.VIDEO_DOWNLOAD_SUCCESS);
        UCCore.a(this.a, this.b);
    }
}
