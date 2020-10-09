package com.taobao.tao.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TBStatusBarUtils {
    public static int toGrey(int i) {
        return (((((i & 16711680) >> 16) * 38) + (((65280 & i) >> 8) * 75)) + ((i & 255) * 15)) >> 7;
    }

    public static boolean isBlackColor(int i, int i2) {
        return toGrey(i) < i2;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    protected static boolean miuiSetStatusBarLightMode(Activity activity, boolean z) {
        if (activity == null) {
            return false;
        }
        try {
            Window window = activity.getWindow();
            Class<?> cls = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = cls.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls);
            Method method = window.getClass().getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
            if (z) {
                method.invoke(window, new Object[]{Integer.valueOf(i), Integer.valueOf(i)});
            } else {
                method.invoke(window, new Object[]{0, Integer.valueOf(i)});
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected static void setStatusBarDarkIcon(Activity activity, boolean z) {
        setStatusBarDarkIcon(activity, z, true);
    }

    private static void setStatusBarDarkIcon(View view, boolean z) {
        try {
            int i = View.class.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR").getInt((Object) null);
            int systemUiVisibility = view.getSystemUiVisibility();
            int i2 = z ? systemUiVisibility | i : (i ^ -1) & systemUiVisibility;
            if (i2 != systemUiVisibility) {
                view.setSystemUiVisibility(i2);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    private static void setStatusBarDarkIcon(Window window, boolean z) {
        if (!isLollipop()) {
            changeMeizuFlag(window.getAttributes(), "MEIZU_FLAG_DARK_STATUS_BAR_ICON", z);
            return;
        }
        View decorView = window.getDecorView();
        if (decorView != null) {
            setStatusBarDarkIcon(decorView, z);
        }
    }

    private static void setStatusBarDarkIcon(Activity activity, boolean z, boolean z2) {
        if (activity != null) {
            Class<Activity> cls = Activity.class;
            try {
                Method method = cls.getMethod("setStatusBarDarkIcon", new Class[]{Boolean.TYPE});
                if (method != null) {
                    method.invoke(activity, new Object[]{Boolean.valueOf(z)});
                } else if (z2) {
                    setStatusBarDarkIcon(activity.getWindow(), z);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            }
        }
    }

    public static boolean isMIUIDevice() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    public static boolean isMeizuDevice() {
        return "Meizu".endsWith(Build.MANUFACTURER);
    }

    private static boolean changeMeizuFlag(WindowManager.LayoutParams layoutParams, String str, boolean z) {
        try {
            Field declaredField = layoutParams.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            int i = declaredField.getInt(layoutParams);
            Field declaredField2 = layoutParams.getClass().getDeclaredField("meizuFlags");
            declaredField2.setAccessible(true);
            int i2 = declaredField2.getInt(layoutParams);
            int i3 = z ? i | i2 : (i ^ -1) & i2;
            if (i2 == i3) {
                return false;
            }
            declaredField2.setInt(layoutParams, i3);
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}
