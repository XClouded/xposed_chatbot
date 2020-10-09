package com.ali.user.mobile.base.ui;

import android.app.Activity;
import android.os.Build;

public class AndroidMHelper implements IStatusBarFontHelper {
    public boolean setStatusBarLightMode(Activity activity, boolean z) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        if (z) {
            activity.getWindow().getDecorView().setSystemUiVisibility(8192);
            return true;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(0);
        return true;
    }
}
