package com.taobao.android.dinamicx;

import android.os.Build;
import android.view.View;
import androidx.annotation.ColorInt;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DXDarkModeCenter {
    public static final int DRAW_TYPE_BACKGROUND = 1;
    public static final int DRAW_TYPE_TEXT = 0;
    public static final int DRAW_TYPE_UNSPECIFIED = 2;
    static IDXDarkModeInterface dxDarkModeInterface;
    static boolean enableDarkModeSupport;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXDrawType {
    }

    public static boolean isDark() {
        if (DinamicXEngine.getApplicationContext() == null) {
            return false;
        }
        if (hasSwitchInterface()) {
            return dxDarkModeInterface.isDark(DinamicXEngine.getApplicationContext());
        }
        if ((DinamicXEngine.getApplicationContext().getResources().getConfiguration().uiMode & 48) == 32) {
            return true;
        }
        return false;
    }

    @ColorInt
    public static int switchDarkModeColor(int i, @ColorInt int i2) {
        return dxDarkModeInterface != null ? dxDarkModeInterface.switchDarkModeColor(i, i2) : i2;
    }

    public static boolean isSupportDarkMode() {
        return enableDarkModeSupport;
    }

    public static boolean hasSwitchInterface() {
        return dxDarkModeInterface != null;
    }

    public static void disableForceDark(View view) {
        if (hasSwitchInterface()) {
            dxDarkModeInterface.disableForceDark(view);
        } else if (Build.VERSION.SDK_INT >= 29) {
            view.setForceDarkAllowed(false);
        }
    }
}
