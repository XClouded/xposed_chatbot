package com.alipay.sdk.widget;

import android.view.View;
import com.alipay.sdk.widget.WebViewWindow;

class q implements View.OnClickListener {
    final /* synthetic */ WebViewWindow a;

    q(WebViewWindow webViewWindow) {
        this.a = webViewWindow;
    }

    public void onClick(View view) {
        WebViewWindow.c a2 = this.a.i;
        if (a2 != null) {
            view.setEnabled(false);
            WebViewWindow.f.postDelayed(new r(this, view), 256);
            if (view == this.a.a) {
                a2.a(this.a);
            } else if (view == this.a.c) {
                a2.b(this.a);
            }
        }
    }
}
