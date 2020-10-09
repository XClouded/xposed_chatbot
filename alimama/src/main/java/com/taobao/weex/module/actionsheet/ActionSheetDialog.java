package com.taobao.weex.module.actionsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

public class ActionSheetDialog extends Dialog {
    private BackPressHandler mBackPressHandler;

    public interface BackPressHandler {
        boolean onBackPressed();
    }

    public ActionSheetDialog(@NonNull Context context) {
        super(context);
    }

    public ActionSheetDialog(@NonNull Context context, @StyleRes int i) {
        super(context, i);
    }

    protected ActionSheetDialog(@NonNull Context context, boolean z, @Nullable DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
    }

    public void setBackPressHandler(BackPressHandler backPressHandler) {
        this.mBackPressHandler = backPressHandler;
    }

    public void onBackPressed() {
        if (this.mBackPressHandler == null || !this.mBackPressHandler.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
