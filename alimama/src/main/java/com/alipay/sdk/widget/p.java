package com.alipay.sdk.widget;

import android.content.DialogInterface;
import com.alipay.sdk.app.j;

class p implements DialogInterface.OnClickListener {
    final /* synthetic */ n a;

    p(n nVar) {
        this.a = nVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.a.cancel();
        boolean unused = this.a.b.w = false;
        j.a(j.c());
        this.a.b.a.finish();
    }
}
