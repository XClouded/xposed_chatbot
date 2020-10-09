package com.alipay.sdk.auth;

import android.content.DialogInterface;

class e implements DialogInterface.OnClickListener {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        boolean unused = AuthActivity.this.g = true;
        this.a.a.proceed();
        dialogInterface.dismiss();
    }
}
