package com.ali.telescope.internal.plugins.anr.sharedpreferences;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class ContextImplProxy extends ContextWrapper {
    private static final String TAG = "ContextHook";
    private final Context real;

    public ContextImplProxy(Context context) {
        super(context);
        this.real = context;
    }

    public SharedPreferences getSharedPreferences(String str, int i) {
        return new SharedPreferenceWrapper(super.getSharedPreferences(str, i));
    }

    public Context getBaseContext() {
        if (this.real instanceof ContextWrapper) {
            return ((ContextWrapper) this.real).getBaseContext();
        }
        return super.getBaseContext();
    }
}
