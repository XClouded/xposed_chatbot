package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.alipay.sdk.util.e;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class ce implements ValueCallback<UpdateTask> {
    final /* synthetic */ bo a;
    final /* synthetic */ bw b;

    ce(bw bwVar, bo boVar) {
        this.b = bwVar;
        this.a = boVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UpdateTask updateTask = (UpdateTask) obj;
        Log.d(bw.e, e.a);
        synchronized (this.b) {
            boolean unused = this.b.g = true;
        }
        updateTask.delete();
        this.a.a(3, updateTask.getException());
    }
}
