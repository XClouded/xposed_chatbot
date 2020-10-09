package com.alipay.sdk.widget;

import com.alipay.sdk.util.c;
import com.alipay.sdk.widget.a;

class b implements Runnable {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void run() {
        if (this.a.e == null) {
            a.C0003a unused = this.a.e = new a.C0003a(this.a.f);
            this.a.e.setCancelable(this.a.k);
        }
        try {
            if (!this.a.e.isShowing()) {
                this.a.e.show();
                this.a.l.sendEmptyMessageDelayed(1, 15000);
            }
        } catch (Exception e) {
            c.a(e);
        }
    }
}
