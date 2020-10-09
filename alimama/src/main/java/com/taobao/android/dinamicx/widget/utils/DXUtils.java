package com.taobao.android.dinamicx.widget.utils;

public class DXUtils {
    public static final int SCREEN_WIDTH = 1125;

    public static int transformToNativeGravity(int i) {
        if (i == 0) {
            return 51;
        }
        if (i == 1) {
            return 19;
        }
        if (i == 2) {
            return 83;
        }
        if (i == 3) {
            return 49;
        }
        if (i == 4) {
            return 17;
        }
        if (i == 5) {
            return 81;
        }
        if (i == 6) {
            return 53;
        }
        if (i == 7) {
            return 21;
        }
        return i == 8 ? 85 : 51;
    }
}
