package com.uc.webview.export;

import android.view.KeyEvent;
import android.view.View;
import com.uc.webview.export.extension.UCCore;

/* compiled from: U4Source */
final class d implements View.OnKeyListener {
    final /* synthetic */ View.OnKeyListener a;
    final /* synthetic */ WebView b;
    private View.OnKeyListener c = this.a;

    d(WebView webView, View.OnKeyListener onKeyListener) {
        this.b = webView;
        this.a = onKeyListener;
    }

    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (this.c == null) {
            return false;
        }
        if (com.uc.webview.export.internal.utility.d.a().b(UCCore.ENABLE_WEBVIEW_LISTENER_STANDARDIZATION_OPTION)) {
            return this.c.onKey(this.b, i, keyEvent);
        }
        return this.c.onKey(view, i, keyEvent);
    }
}
