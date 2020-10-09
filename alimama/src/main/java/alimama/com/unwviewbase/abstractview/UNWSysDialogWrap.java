package alimama.com.unwviewbase.abstractview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UNWSysDialogWrap extends UNWAbstractDialog {
    private AlertDialog dialog;

    public UNWSysDialogWrap(Context context) {
        super(context);
    }

    public UNWSysDialogWrap(Context context, int i) {
        super(context, i);
    }

    protected UNWSysDialogWrap(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
    }

    public void setDialog(AlertDialog alertDialog) {
        this.dialog = alertDialog;
    }

    public boolean doShow() {
        try {
            if (!(this.mContext instanceof Activity)) {
                return false;
            }
            if (!((Activity) this.mContext).isFinishing() && this.dialog != null) {
                this.dialog.show();
                return true;
            } else if (this.recycle == null) {
                return false;
            } else {
                this.recycle.onShow(this);
                return false;
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public void show() {
        try {
            if (this.mContext != null) {
                if (this.dialog != null) {
                    this.dialog.show();
                }
                if (this.recycle != null) {
                    this.recycle.onShow(this);
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void cancel() {
        try {
            if ((this.mContext instanceof Activity) && !((Activity) this.mContext).isFinishing() && this.dialog != null) {
                this.dialog.cancel();
            }
            if (this.recycle != null) {
                this.recycle.dismiss(this, true);
            }
        } catch (Exception unused) {
        }
    }

    public void unactiveDismiss() {
        try {
            if ((this.mContext instanceof Activity) && !((Activity) this.mContext).isFinishing() && this.dialog != null) {
                this.dialog.dismiss();
            }
            if (this.recycle != null) {
                this.recycle.dismiss(this, false);
            }
        } catch (Exception unused) {
        }
    }

    public void dismiss() {
        try {
            if (!(this.mContext instanceof Activity)) {
                this.dialog.dismiss();
            } else if (!((Activity) this.mContext).isFinishing() && this.dialog != null) {
                this.dialog.dismiss();
            }
            if (this.recycle != null) {
                this.recycle.dismiss(this, true);
            }
        } catch (Exception unused) {
        }
    }

    public boolean isShowing() {
        if (this.dialog == null || !this.dialog.isShowing()) {
            return false;
        }
        return this.dialog.isShowing();
    }
}
