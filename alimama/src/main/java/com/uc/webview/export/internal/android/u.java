package com.uc.webview.export.internal.android;

import com.uc.webview.export.internal.a;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
public final class u extends a {
    Runnable f = new v(this);

    public final void a(IWebView iWebView, int i) {
        if (i == 0) {
            if (d != 1) {
                d = 1;
            }
        } else if (d == 1) {
            e.removeCallbacks(this.f);
            e.post(this.f);
        }
    }

    public final void b(IWebView iWebView) {
        a.remove(iWebView);
        if (a.isEmpty()) {
            if (IWaStat.WaStat.getPrintLogEnable()) {
                Log.d("SDKWaStat", "WebViewDetector:destroy");
            }
            IWaStat.WaStat.saveData(true);
        }
    }
}
