package com.ali.user.mobile.base.ui;

import android.app.Activity;
import android.view.Window;
import java.lang.reflect.Method;

public class MIUIHelper implements IStatusBarFontHelper {
    public boolean setStatusBarLightMode(Activity activity, boolean z) {
        Window window = activity.getWindow();
        if (window != null) {
            Class<?> cls = window.getClass();
            try {
                Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
                Method method = cls.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                if (z) {
                    method.invoke(window, new Object[]{Integer.valueOf(i), Integer.valueOf(i)});
                } else {
                    method.invoke(window, new Object[]{0, Integer.valueOf(i)});
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }
}
