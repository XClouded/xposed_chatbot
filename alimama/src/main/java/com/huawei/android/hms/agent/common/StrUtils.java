package com.huawei.android.hms.agent.common;

import com.taobao.weex.BuildConfig;

public final class StrUtils {
    public static String objDesc(Object obj) {
        if (obj == null) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
        return obj.getClass().getName() + '@' + Integer.toHexString(obj.hashCode());
    }
}
