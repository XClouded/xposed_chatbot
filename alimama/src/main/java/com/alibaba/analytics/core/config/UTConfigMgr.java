package com.alibaba.analytics.core.config;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import java.util.HashMap;
import java.util.Map;

public class UTConfigMgr {
    private static final int BROADCAST_SAFE_MAX_LENGTH = 100000;
    static final String INTENT_CONFIG_CHANGE = "com.alibaba.analytics.config.change";
    static final String INTENT_EXTRA_KEY = "key";
    static final String INTENT_EXTRA_VALUE = "value";
    private static final String TAG = "UTConfigMgr";
    private static Map<String, String> configMap = new HashMap();

    public static synchronized void postServerConfig(String str, String str2) {
        synchronized (UTConfigMgr.class) {
            try {
                Context context = Variables.getInstance().getContext();
                if (context == null) {
                    context = ClientVariables.getInstance().getContext();
                }
                if (context != null) {
                    if (TextUtils.isEmpty(str2) || str2.length() <= 100000) {
                        configMap.put(str, str2);
                        String packageName = context.getPackageName();
                        Logger.d(TAG, "postServerConfig packageName", packageName, "key", str, "value", str2);
                        Intent intent = new Intent(INTENT_CONFIG_CHANGE);
                        intent.setPackage(packageName);
                        intent.putExtra("key", str);
                        intent.putExtra("value", str2);
                        context.sendBroadcast(intent);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } catch (Throwable th) {
                Logger.e(TAG, th, new Object[0]);
            }
        }
        return;
    }

    public static synchronized void postAllServerConfig() {
        synchronized (UTConfigMgr.class) {
            for (Map.Entry next : configMap.entrySet()) {
                postServerConfig((String) next.getKey(), (String) next.getValue());
            }
        }
    }
}
