package alimama.com.unwviewbase.abstractview;

import alimama.com.unwviewbase.interfaces.DialogLifeRecycle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import java.io.StringWriter;
import java.util.List;

public class UNWAbstractDialog extends Dialog {
    public String endtime = "";
    public long fatigueTime = 0;
    Context mContext;
    public List<String> pageName;
    public int priority = 0;
    DialogLifeRecycle recycle;
    public String starttime = "";
    public String type;
    public String uuid;

    public void setRecycle(DialogLifeRecycle dialogLifeRecycle) {
        this.recycle = dialogLifeRecycle;
    }

    public UNWAbstractDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public UNWAbstractDialog(Context context, int i) {
        super(context, i);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
    }

    protected UNWAbstractDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        this.mContext = context;
    }

    public boolean doShow() {
        try {
            if (!(this.mContext instanceof Activity)) {
                return false;
            }
            Activity activity = (Activity) this.mContext;
            if (activity != null && !activity.isFinishing()) {
                super.show();
                return true;
            } else if (this.recycle == null) {
                return false;
            } else {
                this.recycle.onShow(this);
                return false;
            }
        } catch (Exception unused) {
            new StringWriter();
            return false;
        }
    }

    public void show() {
        if (this.mContext != null) {
            if (!(this.mContext instanceof Activity)) {
                super.show();
            } else if (!((Activity) this.mContext).isFinishing()) {
                super.show();
            }
        }
        if (this.recycle != null) {
            this.recycle.onShow(this);
        }
    }

    public void cancel() {
        try {
            if ((this.mContext instanceof Activity) && !((Activity) this.mContext).isFinishing()) {
                super.cancel();
            }
        } catch (Exception unused) {
        }
    }

    public void unactiveDismiss() {
        try {
            if ((this.mContext instanceof Activity) && !((Activity) this.mContext).isFinishing()) {
                super.dismiss();
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
                super.dismiss();
            } else if (!((Activity) this.mContext).isFinishing()) {
                super.dismiss();
            }
            if (this.recycle != null) {
                this.recycle.dismiss(this, true);
            }
        } catch (Exception unused) {
        }
    }
}
