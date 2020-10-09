package com.huawei.hms.b;

import android.content.DialogInterface;

/* compiled from: AbstractDialog */
class b implements DialogInterface.OnClickListener {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.d();
    }
}
