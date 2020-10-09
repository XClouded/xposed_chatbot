package com.alipay.sdk.app;

import android.content.DialogInterface;

class e implements DialogInterface.OnClickListener {
    final /* synthetic */ c a;

    e(c cVar) {
        this.a = cVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.b.cancel();
        boolean unused = this.a.c.b = false;
        j.a(j.c());
        this.a.a.finish();
    }
}
