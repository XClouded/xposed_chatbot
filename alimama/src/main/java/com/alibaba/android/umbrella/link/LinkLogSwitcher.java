package com.alibaba.android.umbrella.link;

import android.util.Log;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.util.UMConfigHelper;
import java.io.File;

final class LinkLogSwitcher {
    private static final String DEFAULT_VERSION = "unknown";
    private static final String GROUP_UMBRELLA_TRACE_2 = "umbrella_trace2";
    private static final String KEY_CF_TEMPLATE_FULL = "CF_";
    private static final String KEY_CS_TEMPLATE_FULL = "CS_";
    private static final String KEY_DC_TEMPLATE_FULL = "DC_";
    private static final String KEY_ENABLE_FEEDBACK = "enableFeedback";
    private static final String KEY_ENABLE_LOG = "enableLogcat";
    private static final String KEY_G_TEMPLATE_FULL = "G_";
    private static final String KEY_INFO_TEMPLATE = "I_";
    private static final String KEY_MAX_LINK_COUNT = "maxLinkCount";
    private static final int SAMPLING_GREATER_THAN_CONFIG = 1;
    private static final int SAMPLING_LESS_THAN_CONFIG = 0;
    private static final int SAMPLING_NOT_FOUND_CONFIG = 2;
    private static final String TAG = "umbrella";
    private static final String V_CONST_ANY = "ANY";
    private static final int V_LINK_CACHE_DEFAULT_MAX_SIZE = 300;
    private static final int V_LINK_CACHE_MIN_SIZE = 30;
    private final UMConfigHelper configHelper = new UMConfigHelper(GROUP_UMBRELLA_TRACE_2);
    private String filePath = "/data/local/tmp/.com_taobao_taobao_umbrella_switcher";
    private boolean switcherFilePass = false;

    LinkLogSwitcher() {
        try {
            this.switcherFilePass = new File(this.filePath).exists();
            Object[] objArr = new Object[2];
            objArr[0] = this.filePath;
            objArr[1] = this.switcherFilePass ? "存在" : "不存在";
            Log.e(TAG, String.format("%s %s", objArr));
        } catch (Throwable th) {
            Log.e(TAG, th.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public int getLinkCacheMaxSize() {
        Integer num = this.configHelper.getInt(KEY_MAX_LINK_COUNT);
        if (num == null) {
            return 300;
        }
        return Math.max(30, num.intValue());
    }

    /* access modifiers changed from: package-private */
    public boolean isLogcatEnabled() {
        if (filePass()) {
            return true;
        }
        Boolean bool = this.configHelper.getBool(KEY_ENABLE_LOG);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: package-private */
    public boolean isFeedbackEnabled() {
        Boolean bool;
        if (!filePass() && (bool = this.configHelper.getBool(KEY_ENABLE_FEEDBACK)) != null) {
            return bool.booleanValue();
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isSkipAllPoint(String str, String str2) {
        if (filePass()) {
            return false;
        }
        return matchAnyConfig(false, KEY_G_TEMPLATE_FULL + str, "G_ANY");
    }

    /* access modifiers changed from: package-private */
    public boolean isSkipLog(String str, String str2, String str3, String str4) {
        if (filePass()) {
            return false;
        }
        double random = Math.random();
        return isRandomGreaterThanAnyConfig(random, 0.0d, KEY_INFO_TEMPLATE + str, "I_ANY");
    }

    /* access modifiers changed from: package-private */
    public boolean isSkipCommit(String str, String str2, String str3, String str4) {
        if (filePass()) {
            return false;
        }
        double random = Math.random();
        if (UMStringUtils.isNotEmpty(str4)) {
            return isRandomGreaterThanAnyConfig(random, 0.0d, KEY_CF_TEMPLATE_FULL + str, "E_" + str4, "CF_ANY");
        }
        return isRandomGreaterThanAnyConfig(random, 0.0d, KEY_CS_TEMPLATE_FULL + str, "CS_ANY");
    }

    /* access modifiers changed from: package-private */
    public boolean isNeedDoubleCheckCommit(String str, String str2, String str3) {
        return matchAnyConfig(false, KEY_DC_TEMPLATE_FULL + str, "DC_ANY");
    }

    private boolean matchAnyConfig(boolean z, String... strArr) {
        Boolean bool;
        if (!(strArr == null || strArr.length == 0)) {
            for (String str : strArr) {
                if (!UMStringUtils.isEmpty(str) && (bool = this.configHelper.getBool(str)) != null) {
                    return bool.booleanValue();
                }
            }
        }
        return z;
    }

    private boolean isRandomGreaterThanAnyConfig(double d, double d2, String... strArr) {
        if (!(strArr == null || strArr.length == 0)) {
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                int computeRandomResult = computeRandomResult(d, strArr[i]);
                if (2 == computeRandomResult) {
                    i++;
                } else if (1 == computeRandomResult) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (d > d2) {
            return true;
        }
        return false;
    }

    private int computeRandomResult(double d, String str) {
        double d2 = this.configHelper.getDouble(str, -1.0d);
        if (d2 == -1.0d) {
            return 2;
        }
        return d > d2 ? 1 : 0;
    }

    private static String getValueOrAny(@Nullable String str) {
        return UMStringUtils.isNotEmpty(str) ? str : V_CONST_ANY;
    }

    private boolean filePass() {
        return this.switcherFilePass;
    }
}
