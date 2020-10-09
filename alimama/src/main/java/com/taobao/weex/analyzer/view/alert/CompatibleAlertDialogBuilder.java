package com.taobao.weex.analyzer.view.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

public class CompatibleAlertDialogBuilder extends AlertDialog.Builder {
    public CompatibleAlertDialogBuilder(Context context) {
        super(context);
    }

    public AlertDialog create() {
        AlertDialog create = super.create();
        try {
            if (create.getWindow() != null && Build.VERSION.SDK_INT <= 19) {
                create.getWindow().setType(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return create;
    }
}
