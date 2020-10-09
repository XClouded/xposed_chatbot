package com.ali.telescope.internal.plugins.fdoverflow;

import androidx.annotation.Keep;

@Keep
public class FdInfoFetcher {
    public static native int getCurrentFdNum();

    public static native int getFdLimit();

    public static native String[] getFileList();
}
