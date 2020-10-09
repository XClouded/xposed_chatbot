package com.taobao.bspatch;

import android.os.Environment;
import java.io.File;

public class BSPatch {
    public static final String ROOT = (Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS);

    public static native int bspatch(String str, String str2, String str3);

    static {
        try {
            System.loadLibrary("BSPatch");
        } catch (Throwable unused) {
        }
    }
}
