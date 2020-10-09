package com.alipay.sdk.widget;

class c implements Runnable {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    public void run() {
        if (this.a.e != null && this.a.e.isShowing()) {
            try {
                this.a.l.removeMessages(1);
                this.a.e.dismiss();
            } catch (Exception e) {
                com.alipay.sdk.util.c.a(e);
            }
        }
    }
}
