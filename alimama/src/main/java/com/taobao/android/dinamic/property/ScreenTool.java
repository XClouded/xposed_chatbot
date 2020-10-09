package com.taobao.android.dinamic.property;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.log.DinamicLog;

public class ScreenTool {
    private static float DENSITY = -1.0f;
    private static final String SUFFIX_AP = "ap";
    private static final String SUFFIX_NP = "np";
    private static final int WIDTH_REFER = 375;
    private static int WIDTH_SCREEN = -1;

    public static int getPx(Context context, Object obj, int i) {
        if (obj == null) {
            return i;
        }
        String lowerCase = String.valueOf(obj).toLowerCase();
        if (TextUtils.isEmpty(lowerCase)) {
            if (Dinamic.isDebugable()) {
                DinamicLog.d(Dinamic.TAG, "size属性为空字符串");
            }
            return i;
        }
        try {
            int screenWidth = getScreenWidth(context);
            float density = getDensity(context);
            if (lowerCase.contains("np")) {
                return (int) (Float.valueOf(Float.parseFloat(lowerCase.replace("np", ""))).floatValue() * density);
            }
            if (lowerCase.contains("ap")) {
                return Math.round(((float) screenWidth) * (Float.valueOf(Float.parseFloat(lowerCase.replace("ap", ""))).floatValue() / 375.0f));
            }
            return Math.round(((float) screenWidth) * (Float.parseFloat(lowerCase) / 375.0f));
        } catch (NumberFormatException unused) {
            if (Dinamic.isDebugable()) {
                DinamicLog.w(Dinamic.TAG, (String) obj, "写法错误，解析出错");
            }
            return i;
        }
    }

    private static float getDensity(Context context) {
        if (DENSITY < 0.0f) {
            DENSITY = context.getResources().getDisplayMetrics().density;
        }
        return DENSITY;
    }

    public static int getScreenWidth(Context context) {
        if (WIDTH_SCREEN < 0) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            WIDTH_SCREEN = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        return WIDTH_SCREEN;
    }

    public static int dip2px(Context context, float f) {
        return (int) (f * context.getResources().getDisplayMetrics().density);
    }

    public static int px2sp(Context context, float f) {
        return (int) (f / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int sp2px(Context context, float f) {
        return (int) (f * context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static void forceResetScreenSize() {
        WIDTH_SCREEN = -1;
        DENSITY = -1.0f;
        if (Dinamic.getContext() != null) {
            getScreenWidth(Dinamic.getContext());
            getDensity(Dinamic.getContext());
        }
    }

    public static boolean checkScreenWidthChanged() {
        Resources resources;
        DisplayMetrics displayMetrics;
        Context context = Dinamic.getContext();
        if (context == null || (resources = context.getResources()) == null || (displayMetrics = resources.getDisplayMetrics()) == null || Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) == getScreenWidth(context)) {
            return false;
        }
        return true;
    }
}
