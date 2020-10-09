package com.uc.webview.export;

import android.view.View;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.d;

/* compiled from: U4Source */
final class b implements View.OnLongClickListener {
    final /* synthetic */ View.OnLongClickListener a;
    final /* synthetic */ WebView b;
    private View.OnLongClickListener c = this.a;

    b(WebView webView, View.OnLongClickListener onLongClickListener) {
        this.b = webView;
        this.a = onLongClickListener;
    }

    public final boolean onLongClick(View view) {
        if (this.c == null) {
            return false;
        }
        if (d.a().b(UCCore.ENABLE_WEBVIEW_LISTENER_STANDARDIZATION_OPTION)) {
            return this.c.onLongClick(this.b);
        }
        return this.c.onLongClick(view);
    }
}
