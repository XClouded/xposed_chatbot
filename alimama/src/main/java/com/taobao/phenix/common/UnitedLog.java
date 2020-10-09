package com.taobao.phenix.common;

import android.text.TextUtils;
import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.tcommon.log.FLog;
import com.taobao.weex.el.parse.Operators;

public class UnitedLog extends FLog {
    private static final String FIRST_TAG = "RxPhenix";

    private static void completeHttpPrefix(StringBuilder sb, String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!z || !str.startsWith("//")) {
            sb.append(str.replace(Operators.MOD, "%%"));
            return;
        }
        sb.append(CommonUtils.HTTP_PRE);
        sb.append(str.replace(Operators.MOD, "%%"));
        sb.append("?complete=prefix");
    }

    private static String standardizing(String str, String str2) {
        return standardizing(str, -1, (String) null, (String) null, str2, false);
    }

    private static String standardizing(String str, ImageRequest imageRequest, String str2, boolean z) {
        if (imageRequest == null) {
            return standardizing(str, -1, (String) null, (String) null, str2, false);
        }
        return standardizing(str, imageRequest.getId(), imageRequest.getModuleName(), imageRequest.getPath(), str2, z);
    }

    private static int predictOutputSize(String str, int i, String str2, String str3, String str4, boolean z) {
        int i2 = 5;
        if (str != null) {
            i2 = 5 + str.length();
        }
        if (str4 != null) {
            i2 += str4.length() + 2;
        }
        if (i > 0) {
            i2 += 11;
        }
        if (str2 != null) {
            i2 += str2.length() + 8;
        }
        if (str3 == null) {
            return i2;
        }
        int length = i2 + str3.length() + 7;
        return z ? length + 21 : length;
    }

    private static String standardizing(String str, int i, String str2, String str3, String str4, boolean z) {
        StringBuilder sb = new StringBuilder(predictOutputSize(str, i, str2, str3, str4, z));
        sb.append(Operators.ARRAY_START);
        sb.append(str4);
        sb.append(Operators.ARRAY_END);
        if (i > 0) {
            sb.append(" ID=");
            sb.append(i);
        }
        if (str2 != null) {
            sb.append(" MODULE=");
            sb.append(str2);
        }
        if (i > 0 || str2 != null) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        } else {
            sb.append(' ');
        }
        sb.append(str);
        if (str3 != null) {
            sb.append(", PATH=");
            completeHttpPrefix(sb, str3, z);
        }
        return sb.substring(0);
    }

    private static String standardizing(String str, String str2, String str3, boolean z) {
        return standardizing(str2, -1, (String) null, str3, str, z);
    }

    public static void vp(String str, String str2, String str3, Object... objArr) {
        if (isLoggable(2)) {
            FLog.v(FIRST_TAG, standardizing(str, str3, str2, true), objArr);
        }
    }

    public static void v(String str, ImageRequest imageRequest, String str2, Object... objArr) {
        if (isLoggable(2)) {
            FLog.v(FIRST_TAG, standardizing(str2, imageRequest, str, true), objArr);
        }
    }

    public static void v(String str, String str2, Object... objArr) {
        if (isLoggable(2)) {
            FLog.v(FIRST_TAG, standardizing(str2, str), objArr);
        }
    }

    public static void dp(String str, String str2, String str3, Object... objArr) {
        if (isLoggable(3)) {
            FLog.d(FIRST_TAG, standardizing(str, str3, str2, true), objArr);
        }
    }

    public static void d(String str, ImageRequest imageRequest, String str2, Object... objArr) {
        if (isLoggable(3)) {
            FLog.d(FIRST_TAG, standardizing(str2, imageRequest, str, true), objArr);
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (isLoggable(3)) {
            FLog.d(FIRST_TAG, standardizing(str2, str), objArr);
        }
    }

    public static void ip(String str, String str2, String str3, Object... objArr) {
        if (isLoggable(4)) {
            FLog.i(FIRST_TAG, standardizing(str, str3, str2, true), objArr);
        }
    }

    public static void i(String str, ImageRequest imageRequest, String str2, Object... objArr) {
        if (isLoggable(4)) {
            FLog.i(FIRST_TAG, standardizing(str2, imageRequest, str, true), objArr);
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (isLoggable(4)) {
            FLog.i(FIRST_TAG, standardizing(str2, str), objArr);
        }
    }

    public static void wp(String str, String str2, String str3, Object... objArr) {
        if (isLoggable(5)) {
            FLog.w(FIRST_TAG, standardizing(str, str3, str2, false), objArr);
        }
    }

    public static void w(String str, ImageRequest imageRequest, String str2, Object... objArr) {
        if (isLoggable(5)) {
            FLog.w(FIRST_TAG, standardizing(str2, imageRequest, str, false), objArr);
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (isLoggable(5)) {
            FLog.w(FIRST_TAG, standardizing(str2, str), objArr);
        }
    }

    public static void ep(String str, String str2, String str3, Object... objArr) {
        if (isLoggable(6)) {
            FLog.e(FIRST_TAG, standardizing(str, str3, str2, false), objArr);
        }
    }

    public static void e(String str, ImageRequest imageRequest, String str2, Object... objArr) {
        if (isLoggable(6)) {
            FLog.e(FIRST_TAG, standardizing(str2, imageRequest, str, false), objArr);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (isLoggable(6)) {
            FLog.e(FIRST_TAG, standardizing(str2, str), objArr);
        }
    }

    public static void e(String str, String str2, ImageRequest imageRequest) {
        if (isLoggable(6)) {
            FLog.e(6, str, "[traceId:" + imageRequest.getStatistics().mFullTraceId + "] | " + "[requestId:" + imageRequest.getId() + "] |" + str2 + "| " + imageRequest.getPath());
        }
    }
}
