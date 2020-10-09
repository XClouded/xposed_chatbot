package com.ali.telescope.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;

public class TeleScopeSharePreferences {
    private static Singleton<TeleScopeSharePreferences> sTeleScopeSharePreferences = new Singleton<TeleScopeSharePreferences>() {
        /* access modifiers changed from: protected */
        public TeleScopeSharePreferences create() {
            return new TeleScopeSharePreferences();
        }
    };
    private Map<String, SharedPreferences> processSharedPreferences;

    private TeleScopeSharePreferences() {
        this.processSharedPreferences = new HashMap();
    }

    public static TeleScopeSharePreferences getInstance() {
        return sTeleScopeSharePreferences.get();
    }

    public SharedPreferences getProcessSP(Context context, String str) {
        SharedPreferences sharedPreferences = this.processSharedPreferences.get(str);
        if (sharedPreferences == null) {
            synchronized (TeleScopeSharePreferences.class) {
                sharedPreferences = this.processSharedPreferences.get(str);
                if (sharedPreferences == null) {
                    SharedPreferences sharedPreferences2 = context.getSharedPreferences(str + ProcessUtils.getSimpleProcessName(context), 0);
                    this.processSharedPreferences.put(str, sharedPreferences2);
                    sharedPreferences = sharedPreferences2;
                }
            }
        }
        return sharedPreferences;
    }
}
