package com.taobao.phenix.compat;

import android.util.Log;
import com.taobao.tcommon.log.FastFormatLog;
import com.taobao.tlog.adapter.AdapterForTLog;

public class TFormatLog extends FastFormatLog {
    private int mLastTLogLevel;
    private Integer mMinLevel;
    private int mTLogLevelDetectTotal = -1;
    private boolean mTLogValid = AdapterForTLog.isValid();

    private int switchLevelFromTLogChar(char c) {
        switch (c) {
            case 'D':
                return 3;
            case 'E':
                return 6;
            case 'I':
                return 4;
            case 'L':
                return Integer.MAX_VALUE;
            case 'V':
                return 2;
            case 'W':
                return 5;
            default:
                return 6;
        }
    }

    public void setMinLevel(int i) {
        this.mMinLevel = Integer.valueOf(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001f, code lost:
        if (r0 > 200) goto L_0x0021;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isLoggable(int r5) {
        /*
            r4 = this;
            java.lang.Integer r0 = r4.mMinLevel
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0010
            java.lang.Integer r0 = r4.mMinLevel
            int r0 = r0.intValue()
            if (r5 < r0) goto L_0x000f
            r1 = 1
        L_0x000f:
            return r1
        L_0x0010:
            boolean r0 = r4.mTLogValid
            if (r0 == 0) goto L_0x003f
            int r0 = r4.mTLogLevelDetectTotal
            if (r0 < 0) goto L_0x0021
            int r0 = r4.mTLogLevelDetectTotal
            int r0 = r0 + r2
            r4.mTLogLevelDetectTotal = r0
            r3 = 200(0xc8, float:2.8E-43)
            if (r0 <= r3) goto L_0x0039
        L_0x0021:
            java.lang.String r0 = com.taobao.tlog.adapter.AdapterForTLog.getLogLevel()
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x002d
            r0 = 6
            goto L_0x0035
        L_0x002d:
            char r0 = r0.charAt(r1)
            int r0 = r4.switchLevelFromTLogChar(r0)
        L_0x0035:
            r4.mLastTLogLevel = r0
            r4.mTLogLevelDetectTotal = r1
        L_0x0039:
            int r0 = r4.mLastTLogLevel
            if (r5 < r0) goto L_0x003e
            r1 = 1
        L_0x003e:
            return r1
        L_0x003f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.TFormatLog.isLoggable(int):boolean");
    }

    public void v(String str, String str2, Object... objArr) {
        if (this.mTLogValid) {
            AdapterForTLog.logv(str, fastFormat(str2, objArr));
        } else {
            Log.v(str, fastFormat(str2, objArr));
        }
    }

    public void d(String str, String str2, Object... objArr) {
        if (this.mTLogValid) {
            AdapterForTLog.logd(str, fastFormat(str2, objArr));
        } else {
            Log.d(str, fastFormat(str2, objArr));
        }
    }

    public void i(String str, String str2, Object... objArr) {
        if (this.mTLogValid) {
            AdapterForTLog.logi(str, fastFormat(str2, objArr));
        } else {
            Log.i(str, fastFormat(str2, objArr));
        }
    }

    public void w(String str, String str2, Object... objArr) {
        if (this.mTLogValid) {
            AdapterForTLog.logw(str, fastFormat(str2, objArr));
        } else {
            Log.w(str, fastFormat(str2, objArr));
        }
    }

    public void e(String str, String str2, Object... objArr) {
        if (this.mTLogValid) {
            AdapterForTLog.loge(str, fastFormat(str2, objArr));
        } else {
            Log.e(str, fastFormat(str2, objArr));
        }
    }

    public void e(int i, String str, String str2) {
        if (this.mTLogValid) {
            AdapterForTLog.loge(str, str2);
        } else {
            Log.e(str, str2);
        }
    }
}
