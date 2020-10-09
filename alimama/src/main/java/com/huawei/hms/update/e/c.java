package com.huawei.hms.update.e;

import android.content.DialogInterface;

/* compiled from: AbstractDialog */
class c implements DialogInterface.OnCancelListener {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    public void onCancel(DialogInterface dialogInterface) {
        this.a.d();
    }
}
