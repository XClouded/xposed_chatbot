package com.alipay.sdk.auth;

import android.app.Activity;
import android.content.DialogInterface;

class f implements DialogInterface.OnClickListener {
    final /* synthetic */ d a;

    f(d dVar) {
        this.a = dVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.a.cancel();
        boolean unused = AuthActivity.this.g = false;
        g.a((Activity) AuthActivity.this, AuthActivity.this.d + "?resultCode=150");
        AuthActivity.this.finish();
    }
}
