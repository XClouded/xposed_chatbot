package com.alipay.sdk.widget;

import android.content.DialogInterface;

class o implements DialogInterface.OnClickListener {
    final /* synthetic */ n a;

    o(n nVar) {
        this.a = nVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        boolean unused = this.a.b.w = true;
        this.a.a.proceed();
        dialogInterface.dismiss();
    }
}
