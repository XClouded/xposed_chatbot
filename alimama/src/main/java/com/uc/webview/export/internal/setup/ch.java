package com.uc.webview.export.internal.setup;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class ch implements ValueCallback<UpdateTask> {
    final /* synthetic */ bw a;
    private int b = 3;

    ch(bw bwVar) {
        this.a = bwVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UpdateTask updateTask = (UpdateTask) obj;
        Log.d(bw.e, UCCore.EVENT_EXCEPTION);
        synchronized (this.a) {
            boolean unused = this.a.f = true;
        }
        int i = this.b;
        this.b = i - 1;
        if (i > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(new ci(this, updateTask), 60000);
        }
    }
}
