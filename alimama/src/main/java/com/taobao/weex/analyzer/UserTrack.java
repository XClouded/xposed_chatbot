package com.taobao.weex.analyzer;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.utils.ReflectionUtil;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Map;

public class UserTrack {
    public static void commit(@Nullable Context context, @NonNull String str, @Nullable Map<String, String> map) {
        if (!isUTExists() || context == null) {
            Log.d("weex-analyzer", "ut not exists");
            return;
        }
        Log.d("weex-analyzer", ">>>>> will send ut log :" + str);
        String packageName = context.getPackageName();
        HashMap hashMap = new HashMap(4);
        hashMap.put("appName", packageName);
        if (map != null && !map.isEmpty()) {
            hashMap.putAll(map);
        }
        try {
            UTHitBuilders.UTControlHitBuilder uTControlHitBuilder = new UTHitBuilders.UTControlHitBuilder(str);
            uTControlHitBuilder.setProperties(hashMap);
            UTAnalytics.getInstance().getDefaultTracker().send(uTControlHitBuilder.build());
        } catch (Throwable th) {
            Log.e("weex-analyzer", "ut failed.", th);
        }
    }

    private static boolean isUTExists() {
        return (ReflectionUtil.tryGetClassForName("com.ut.mini.UTAnalytics") == null || ReflectionUtil.tryGetClassForName("com.ut.mini.UTHitBuilders$UTControlHitBuilder") == null) ? false : true;
    }
}
