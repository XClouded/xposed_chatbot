package com.ali.user.mobile.base.ui;

import android.app.Activity;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatusBarHelper {
    public static final int ANDROID_M = 3;
    public static final int FLYME = 2;
    public static final int MIUI = 1;
    public static final int OTHER = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SystemType {
    }

    public static int setStatusBarMode(Activity activity, boolean z) {
        if (Build.VERSION.SDK_INT < 19) {
            return 0;
        }
        if (new MIUIHelper().setStatusBarLightMode(activity, z)) {
            return 1;
        }
        if (new FlymeHelper().setStatusBarLightMode(activity, z)) {
            return 2;
        }
        return new AndroidMHelper().setStatusBarLightMode(activity, z) ? 3 : 0;
    }

    public static void setLightMode(Activity activity, int i) {
        setStatusBarMode(activity, i, true);
    }

    public static void setDarkMode(Activity activity, int i) {
        setStatusBarMode(activity, i, false);
    }

    private static void setStatusBarMode(Activity activity, int i, boolean z) {
        if (i == 1) {
            new MIUIHelper().setStatusBarLightMode(activity, z);
        } else if (i == 2) {
            new FlymeHelper().setStatusBarLightMode(activity, z);
        } else if (i == 3) {
            new AndroidMHelper().setStatusBarLightMode(activity, z);
        }
    }
}
