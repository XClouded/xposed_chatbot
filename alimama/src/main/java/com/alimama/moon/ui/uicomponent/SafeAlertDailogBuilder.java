package com.alimama.moon.ui.uicomponent;

import android.app.Activity;
import android.app.AlertDialog;

public class SafeAlertDailogBuilder extends AlertDialog.Builder {
    private Activity mActivity;

    public SafeAlertDailogBuilder(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    public AlertDialog show() {
        try {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            return super.show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
