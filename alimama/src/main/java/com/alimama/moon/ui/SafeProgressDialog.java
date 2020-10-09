package com.alimama.moon.ui;

import android.app.Activity;
import android.app.ProgressDialog;

public class SafeProgressDialog extends ProgressDialog {
    private Activity mActivity = null;

    public SafeProgressDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    public SafeProgressDialog(Activity activity, int i) {
        super(activity, i);
        this.mActivity = activity;
    }

    public void show() {
        if (this.mActivity != null && !this.mActivity.isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismiss() {
        if (this.mActivity != null && !this.mActivity.isFinishing() && isShowing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
