package com.huawei.hms.b;

import android.content.DialogInterface;
import android.view.KeyEvent;

/* compiled from: AbstractDialog */
class e implements DialogInterface.OnKeyListener {
    final /* synthetic */ a a;

    e(a aVar) {
        this.a = aVar;
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (4 != i || keyEvent.getAction() != 1) {
            return false;
        }
        this.a.a();
        return true;
    }
}
