package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class ai implements ValueCallback<CALLBACK_TYPE> {
    final /* synthetic */ ah a;

    ai(ah ahVar) {
        this.a = ahVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String b = ah.d;
        Log.d(b, ".startCallback MCE(" + this.a.g.exists() + "," + this.a.f.exists() + "," + this.a.h.exists() + Operators.BRACKET_END_STR);
        ah.d(this.a);
        ah.e(this.a);
    }
}
