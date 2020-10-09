package com.alibaba.ut.abtest.internal.util;

import android.content.SharedPreferences;
import com.alibaba.ut.abtest.internal.ABContext;

public final class UTBridge {
    private UTBridge() {
    }

    public static String getChannel() {
        SharedPreferences sharedPreferences = ABContext.getInstance().getContext().getSharedPreferences("ut_setting", 4);
        if (sharedPreferences != null) {
            return sharedPreferences.getString("channel", (String) null);
        }
        return null;
    }
}
