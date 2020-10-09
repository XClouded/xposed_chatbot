package com.alipay.sdk.widget;

import android.view.animation.Animation;
import com.alipay.sdk.widget.j;

class l extends j.a {
    final /* synthetic */ WebViewWindow a;
    final /* synthetic */ j b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    l(j jVar, WebViewWindow webViewWindow) {
        super(jVar, (k) null);
        this.b = jVar;
        this.a = webViewWindow;
    }

    public void onAnimationEnd(Animation animation) {
        this.a.a();
        boolean unused = this.b.v = false;
    }
}
