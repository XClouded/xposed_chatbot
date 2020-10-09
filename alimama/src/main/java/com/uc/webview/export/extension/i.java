package com.uc.webview.export.extension;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;
import java.util.Map;

/* compiled from: U4Source */
final class i implements ValueCallback<UpdateTask> {
    final /* synthetic */ Map a;
    private int b = 3;

    i(Map map) {
        this.a = map;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ValueCallback valueCallback;
        UpdateTask updateTask = (UpdateTask) obj;
        if (!(this.a == null || (valueCallback = (ValueCallback) this.a.get(UCCore.EVENT_EXCEPTION)) == null)) {
            try {
                valueCallback.onReceiveValue((Object) null);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        int i = this.b;
        this.b = i - 1;
        if (i > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(new j(this, updateTask), 60000);
        }
    }
}
