package com.huawei.hms.b;

import android.content.DialogInterface;

/* compiled from: AbstractDialog */
class d implements DialogInterface.OnCancelListener {
    final /* synthetic */ a a;

    d(a aVar) {
        this.a = aVar;
    }

    public void onCancel(DialogInterface dialogInterface) {
        this.a.c();
    }
}
