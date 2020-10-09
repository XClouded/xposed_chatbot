package com.ali.telescope.internal.plugins;

import androidx.annotation.Keep;
import com.ali.telescope.util.TelescopeLog;

@Keep
public class MethodCheck {
    public static native void check();

    static {
        SoLoader.loadHookSo();
        check();
        TelescopeLog.d("check called", new Object[0]);
    }
}
