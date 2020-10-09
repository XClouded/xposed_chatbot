package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class aj implements ValueCallback<CALLBACK_TYPE> {
    final /* synthetic */ ah a;

    aj(ah ahVar) {
        this.a = ahVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String b = ah.d;
        Log.d(b, ".dieCallback MCE(" + this.a.g.exists() + "," + this.a.f.exists() + "," + this.a.h.exists() + Operators.BRACKET_END_STR);
        this.a.a();
        this.a.e.clear();
    }
}
