package com.ali.telescope.internal.plugins.memleak;

import androidx.annotation.Keep;

@Keep
public class MemoryLeak {
    public static native void forkAndAnalyze(String str, String str2, String str3, String str4);

    static {
        System.loadLibrary("telescope_leak_analyzer");
    }
}
