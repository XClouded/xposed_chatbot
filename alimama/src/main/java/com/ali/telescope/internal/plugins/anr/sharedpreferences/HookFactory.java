package com.ali.telescope.internal.plugins.anr.sharedpreferences;

import android.content.Context;

public class HookFactory {
    public static Context createContextImplProxy(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof ContextImplProxy) {
            return context;
        }
        return new ContextImplProxy(context);
    }
}
