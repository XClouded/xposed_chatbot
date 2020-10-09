package com.alibaba.ut.abtest.bucketing.expression;

import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.Utils;

public class VersionUtils {
    private static final String TAG = "VersionUtils";

    public static boolean greaterThan(Object obj, Object obj2) {
        try {
            if (obj instanceof String) {
                if (obj2 instanceof String) {
                    String trim = ((String) obj).trim();
                    String trim2 = ((String) obj2).trim();
                    LogUtils.logD(TAG, "version value greaterThan compare: " + trim + "  " + trim2);
                    String[] split = trim.split("\\.");
                    String[] split2 = trim2.split("\\.");
                    int i = 0;
                    while (i < split.length) {
                        if (i >= split2.length) {
                            return true;
                        }
                        if (split[i].equals(split2[i])) {
                            i++;
                        } else if (Utils.toInt(split[i], 0) > Utils.toInt(split2[i], 0)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    return false;
                }
            }
            return false;
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
            return false;
        }
    }

    public static boolean equals(Object obj, Object obj2) {
        if (!(obj instanceof String) || !(obj2 instanceof String)) {
            return false;
        }
        String trim = ((String) obj).trim();
        String trim2 = ((String) obj2).trim();
        LogUtils.logD(TAG, "version equal compare: " + trim + "  " + trim2);
        return TextUtils.equals(trim, trim2);
    }
}
