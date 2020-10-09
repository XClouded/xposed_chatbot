package com.alimama.unionwl.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LocalDisplay {
    public static int CONTENT_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static int SCREEN_HEIGHT_DP;
    public static int SCREEN_HEIGHT_PIXELS;
    public static int SCREEN_WIDTH_DP;
    public static int SCREEN_WIDTH_PIXELS;
    private static boolean sInitialed;

    public static void init(Context context) {
        if (!sInitialed && context != null) {
            sInitialed = true;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (Version.hasJellyBean_17()) {
                windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            } else {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }
            SCREEN_WIDTH_PIXELS = displayMetrics.widthPixels;
            SCREEN_HEIGHT_PIXELS = displayMetrics.heightPixels;
            SCREEN_DENSITY = displayMetrics.density;
            SCREEN_WIDTH_DP = (int) (((float) SCREEN_WIDTH_PIXELS) / displayMetrics.density);
            SCREEN_HEIGHT_DP = (int) (((float) SCREEN_HEIGHT_PIXELS) / displayMetrics.density);
            CONTENT_HEIGHT_PIXELS = SCREEN_HEIGHT_PIXELS - getStateBarHeight(context);
        }
    }

    private static int getStateBarHeight(Context context) {
        int dp2px = dp2px(20.0f);
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return identifier > 0 ? context.getResources().getDimensionPixelSize(identifier) : dp2px;
    }

    public static int dp2px(float f) {
        return (int) ((f * SCREEN_DENSITY) + 0.5f);
    }

    public static int px2dp(float f) {
        return (int) ((f / SCREEN_DENSITY) + 0.5f);
    }

    public static int designedDP2px(float f) {
        if (SCREEN_WIDTH_DP != 320) {
            f = (f * ((float) SCREEN_WIDTH_DP)) / 320.0f;
        }
        return dp2px(f);
    }

    public static void setPadding(View view, float f, float f2, float f3, float f4) {
        view.setPadding(designedDP2px(f), dp2px(f2), designedDP2px(f3), dp2px(f4));
    }

    public static int getScreenWidthPixels(@NonNull Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        if (displayMetrics == null) {
            return 0;
        }
        return displayMetrics.widthPixels;
    }

    public static int getScreenWidthDp(@NonNull Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        if (displayMetrics == null || displayMetrics.density <= 0.0f) {
            return 0;
        }
        return (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
    }

    @Nullable
    public static DisplayMetrics getDisplayMetrics(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return null;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getWindowRatioHeight(@NonNull Context context, double d) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        if (displayMetrics == null) {
            return 0;
        }
        double d2 = (double) displayMetrics.widthPixels;
        Double.isNaN(d2);
        return (int) (d * d2);
    }
}
