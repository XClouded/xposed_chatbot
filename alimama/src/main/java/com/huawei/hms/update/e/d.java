package com.huawei.hms.update.e;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.huawei.hms.c.h;

/* compiled from: CheckProgress */
public class d extends b {
    public AlertDialog a() {
        ProgressDialog progressDialog = new ProgressDialog(f(), g());
        progressDialog.setMessage(h.d("hms_checking"));
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
