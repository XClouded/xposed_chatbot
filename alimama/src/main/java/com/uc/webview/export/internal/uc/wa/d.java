package com.uc.webview.export.internal.uc.wa;

import com.alimama.union.app.pagerouter.AppPageInfo;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
public final class d implements Runnable {
    final /* synthetic */ a a;

    public d(a aVar) {
        this.a = aVar;
    }

    public final void run() {
        try {
            a.c(this.a);
        } catch (Throwable th) {
            Log.i("SDKWaStat", AppPageInfo.UPDATE, th);
        }
    }
}
