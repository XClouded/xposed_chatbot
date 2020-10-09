package com.alipay.sdk.widget;

import android.view.animation.Animation;
import com.alipay.sdk.widget.j;

class m extends j.a {
    final /* synthetic */ WebViewWindow a;
    final /* synthetic */ String b;
    final /* synthetic */ j c;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    m(j jVar, WebViewWindow webViewWindow, String str) {
        super(jVar, (k) null);
        this.c = jVar;
        this.a = webViewWindow;
        this.b = str;
    }

    public void onAnimationEnd(Animation animation) {
        this.c.removeView(this.a);
        this.c.x.a(this.b);
        boolean unused = this.c.v = false;
    }
}
