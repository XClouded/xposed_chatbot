package com.alibaba.appmonitor.delegate;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.ReflectUtils;
import com.alibaba.analytics.version.UTBuildInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SdkMeta {
    private static final String TAG = "SdkMeta";
    private static final Map<String, String> meta = new ConcurrentHashMap();

    public static Map<String, String> getSDKMetaData() {
        Context context = Variables.getInstance().getContext();
        if (context != null) {
            if (!meta.containsKey("pt")) {
                String string = getString(context, "package_type");
                if (!TextUtils.isEmpty(string)) {
                    meta.put("pt", string);
                } else {
                    meta.put("pt", "");
                }
            }
            if (!meta.containsKey("pid")) {
                String string2 = getString(context, "project_id");
                if (!TextUtils.isEmpty(string2)) {
                    meta.put("pid", string2);
                } else {
                    meta.put("pid", "");
                }
            }
            if (!meta.containsKey("bid")) {
                String string3 = getString(context, "build_id");
                if (!TextUtils.isEmpty(string3)) {
                    meta.put("bid", string3);
                } else {
                    meta.put("bid", "");
                }
            }
            if (!meta.containsKey("bv")) {
                String string4 = getString(context, "base_version");
                if (!TextUtils.isEmpty(string4)) {
                    meta.put("bv", string4);
                } else {
                    meta.put("bv", "");
                }
            }
        }
        String hotPatchVersion = getHotPatchVersion();
        if (!TextUtils.isEmpty(hotPatchVersion)) {
            meta.put("hv", hotPatchVersion);
        } else {
            meta.put("hv", "");
        }
        if (!meta.containsKey("sdk-version")) {
            meta.put("sdk-version", UTBuildInfo.getInstance().getFullSDKVersion());
        }
        return meta;
    }

    private static String getHotPatchVersion() {
        Object invokeMethod;
        try {
            Object invokeStaticMethod = ReflectUtils.invokeStaticMethod("com.taobao.updatecenter.hotpatch.HotPatchManager", "getInstance");
            if (invokeStaticMethod == null || (invokeMethod = ReflectUtils.invokeMethod(invokeStaticMethod, "getPatchSuccessedVersion")) == null) {
                return null;
            }
            return invokeMethod + "";
        } catch (Throwable unused) {
            return null;
        }
    }

    static {
        meta.put("sdk-version", UTBuildInfo.getInstance().getFullSDKVersion());
    }

    public static String getString(Context context, String str) {
        int i;
        if (context == null) {
            return null;
        }
        try {
            i = context.getResources().getIdentifier(str, "string", context.getPackageName());
        } catch (Throwable th) {
            Logger.w(TAG, "getString Id error", th);
            i = 0;
        }
        if (i != 0) {
            return context.getString(i);
        }
        return null;
    }
}
