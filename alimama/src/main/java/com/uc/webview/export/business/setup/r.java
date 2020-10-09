package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class r implements ValueCallback<l> {
    final /* synthetic */ o a;

    r(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        String a2 = o.a;
        Log.d(a2, "die " + lVar);
        IWaStat.WaStat.stat(IWaStat.BUSINESS_DECOMPRESS_AND_ODEX, Long.toString(this.a.b.a), 1);
        if (k.i()) {
            IWaStat.WaStat.saveData();
            IWaStat.WaStat.upload();
        }
        o.a(this.a, lVar);
    }
}
