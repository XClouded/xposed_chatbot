package com.ali.telescope.internal.plugins;

public class SoLoader {
    private static volatile boolean isHookSoLoaded = false;

    public static void loadHookSo() {
        if (!isHookSoLoaded) {
            System.loadLibrary("telescope_hook");
            isHookSoLoaded = true;
        }
    }
}
