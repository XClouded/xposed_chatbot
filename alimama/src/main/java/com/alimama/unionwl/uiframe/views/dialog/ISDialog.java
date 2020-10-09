package com.alimama.unionwl.uiframe.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import java.io.StringWriter;

public class ISDialog extends Dialog {
    private Context mContext;

    public ISDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ISDialog(Context context, int i) {
        super(context, i);
        this.mContext = context;
    }

    protected ISDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        this.mContext = context;
    }

    public boolean doShow() {
        Activity activity;
        try {
            if (!(this.mContext instanceof Activity) || (activity = (Activity) this.mContext) == null || activity.isFinishing()) {
                return false;
            }
            super.show();
            return true;
        } catch (Exception unused) {
            new StringWriter();
            return false;
        }
    }

    public void show() {
        ShowDialogActionQueue.getInstance().tryToShow(this);
    }

    public void cancel() {
        Activity activity;
        try {
            if ((this.mContext instanceof Activity) && (activity = (Activity) this.mContext) != null && !activity.isFinishing()) {
                super.cancel();
            }
        } catch (Exception unused) {
        }
    }

    public void dismiss() {
        Activity activity;
        try {
            if ((this.mContext instanceof Activity) && (activity = (Activity) this.mContext) != null && !activity.isFinishing()) {
                super.dismiss();
            }
        } catch (Exception unused) {
        }
        ShowDialogActionQueue.getInstance().tryToPopUp(this);
    }
}
