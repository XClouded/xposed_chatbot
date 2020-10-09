package com.alimama.unionwl.utils;

import android.graphics.Color;
import android.text.TextUtils;

public class ColorUtils {
    private static final int ERR_COLOR = 0;

    public static int parseColor(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (!str.startsWith("#")) {
            if (!str.startsWith("0x")) {
                return 0;
            }
            str = "#" + str.substring(2);
        }
        try {
            return Color.parseColor(str);
        } catch (Exception unused) {
            return 0;
        }
    }

    public static int adjustAlpha(int i, float f) {
        return Color.argb(Math.round(((float) Color.alpha(i)) * f), Color.red(i), Color.green(i), Color.blue(i));
    }
}
