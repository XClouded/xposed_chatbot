package com.ali.alihadeviceevaluator.old;

import android.os.Build;

public class HardwareOsVersion implements CalScore {
    public int getScore(HardWareInfo hardWareInfo) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            return 10;
        }
        if (i >= 24) {
            return 9;
        }
        if (i >= 23) {
            return 8;
        }
        if (i >= 21) {
            return 7;
        }
        if (i >= 19) {
            return 5;
        }
        if (i >= 18) {
            return 4;
        }
        if (i >= 17) {
            return 3;
        }
        return i >= 16 ? 2 : 1;
    }
}
