package com.huawei.hms.update.e;

import android.app.AlertDialog;
import com.huawei.hms.c.h;

/* compiled from: InstallConfirm */
public class n extends b {
    private String a = h.d("hms_update_title");

    /* access modifiers changed from: protected */
    public AlertDialog a() {
        int c = h.c("hms_update_message_new");
        int c2 = h.c("hms_install");
        AlertDialog.Builder builder = new AlertDialog.Builder(f(), g());
        builder.setMessage(f().getString(c, new Object[]{this.a}));
        builder.setPositiveButton(c2, new o(this));
        builder.setNegativeButton(h.c("hms_cancel"), new p(this));
        return builder.create();
    }

    public void a(String str) {
        this.a = str;
    }
}
