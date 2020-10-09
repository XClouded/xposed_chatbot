package com.alibaba.android.prefetchx;

import android.util.Log;
import com.taobao.weex.el.parse.Operators;

public class PFLog {
    public static void v(String str, Object... objArr) {
        if (PFUtil.isDebug()) {
            Log.v(str, PFUtil.s(objArr));
        }
    }

    public static void d(String str, Object... objArr) {
        if (PFUtil.isDebug()) {
            Log.d(str, PFUtil.s(objArr));
        }
    }

    public static void w(String str, String str2, Throwable... thArr) {
        if (thArr == null || thArr.length <= 0 || thArr[0] == null) {
            Log.e(str, str2);
            return;
        }
        Log.e(str, str2 + getStackTrace(thArr[0]));
    }

    public static void e(String str, String str2) {
        if (PFUtil.isDebug()) {
            Log.e(str, str2);
        }
    }

    public static class Data {
        public static void d(Object... objArr) {
            if (PFUtil.isDebug()) {
                PFLog.d("PrefetchX_Data", PFUtil.s(objArr));
            }
        }

        public static void w(String str, Throwable... thArr) {
            PFLog.w("PrefetchX_Data", str, thArr);
        }
    }

    public static class File {
        public static void d(Object... objArr) {
            if (PFUtil.isDebug()) {
                PFLog.d("PrefetchX_File", PFUtil.s(objArr));
            }
        }

        public static void w(String str, Throwable... thArr) {
            PFLog.w("PrefetchX_File", str, thArr);
        }
    }

    public static class JSModule {
        public static void v(Object... objArr) {
            if (PFUtil.isDebug()) {
                String str = "";
                if (PFUtil.isDebug()) {
                    str = Operators.ARRAY_START_STR + Thread.currentThread().getId() + " - " + Thread.currentThread().getName() + "] ";
                }
                PFLog.v("PrefetchX_JSModule", str + PFUtil.s(objArr));
            }
        }

        public static void d(Object... objArr) {
            if (PFUtil.isDebug()) {
                String str = "";
                if (PFUtil.isDebug()) {
                    str = Operators.ARRAY_START_STR + Thread.currentThread().getId() + " - " + Thread.currentThread().getName() + "] ";
                }
                PFLog.d("PrefetchX_JSModule", str + PFUtil.s(objArr));
            }
        }

        public static void w(String str, Throwable... thArr) {
            String str2 = "";
            if (PFUtil.isDebug()) {
                str2 = Operators.ARRAY_START_STR + Thread.currentThread().getId() + " - " + Thread.currentThread().getName() + "] ";
            }
            PFLog.w("PrefetchX_JSModule", str2 + str, thArr);
        }
    }

    public static class Image {
        public static void d(Object... objArr) {
            if (PFUtil.isDebug()) {
                PFLog.d("PrefetchX_Image", PFUtil.s(objArr));
            }
        }

        public static void w(String str, Throwable... thArr) {
            PFLog.w("PrefetchX_Image", str, thArr);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0032 A[SYNTHETIC, Splitter:B:21:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStackTrace(@androidx.annotation.Nullable java.lang.Throwable r3) {
        /*
            if (r3 != 0) goto L_0x0005
            java.lang.String r3 = ""
            return r3
        L_0x0005:
            r0 = 0
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch:{ all -> 0x002e }
            r1.<init>()     // Catch:{ all -> 0x002e }
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch:{ all -> 0x002c }
            r2.<init>(r1)     // Catch:{ all -> 0x002c }
            r3.printStackTrace(r2)     // Catch:{ all -> 0x0029 }
            r2.flush()     // Catch:{ all -> 0x0029 }
            r1.flush()     // Catch:{ all -> 0x0029 }
            r1.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0021
        L_0x001d:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0021:
            r2.close()
            java.lang.String r3 = r1.toString()
            return r3
        L_0x0029:
            r3 = move-exception
            r0 = r2
            goto L_0x0030
        L_0x002c:
            r3 = move-exception
            goto L_0x0030
        L_0x002e:
            r3 = move-exception
            r1 = r0
        L_0x0030:
            if (r1 == 0) goto L_0x003a
            r1.close()     // Catch:{ IOException -> 0x0036 }
            goto L_0x003a
        L_0x0036:
            r1 = move-exception
            r1.printStackTrace()
        L_0x003a:
            if (r0 == 0) goto L_0x003f
            r0.close()
        L_0x003f:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.PFLog.getStackTrace(java.lang.Throwable):java.lang.String");
    }
}
