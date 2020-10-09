package com.ali.telescope.internal.plugins;

import android.os.Build;

public class PhonePerformanceLevel {
    private static int LEVEL_P = 0;
    public static final int LEVEL_P_HIGH = 1;
    public static final int LEVEL_P_LOW = 3;
    public static final int LEVEL_P_MIDDLE = 2;

    public static int getLevel() {
        if (LEVEL_P != 0) {
            return LEVEL_P;
        }
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            LEVEL_P = 1;
        } else if (i >= 21) {
            LEVEL_P = 2;
        } else {
            LEVEL_P = 3;
        }
        return LEVEL_P;
    }
}
