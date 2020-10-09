package com.uc.webview.export;

import android.view.MotionEvent;
import android.view.View;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.d;

/* compiled from: U4Source */
final class c implements View.OnTouchListener {
    final /* synthetic */ View.OnTouchListener a;
    final /* synthetic */ WebView b;
    private View.OnTouchListener c = this.a;

    c(WebView webView, View.OnTouchListener onTouchListener) {
        this.b = webView;
        this.a = onTouchListener;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.c == null) {
            return false;
        }
        if (d.a().b(UCCore.ENABLE_WEBVIEW_LISTENER_STANDARDIZATION_OPTION)) {
            return this.c.onTouch(this.b, motionEvent);
        }
        return this.c.onTouch(view, motionEvent);
    }
}
