package com.alipay.sdk.app;

import android.content.DialogInterface;

class d implements DialogInterface.OnClickListener {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        boolean unused = this.a.c.b = true;
        this.a.b.proceed();
        dialogInterface.dismiss();
    }
}
