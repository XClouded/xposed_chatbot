package com.taobao.android.dinamicx.exception;

import com.taobao.android.dinamicx.DinamicXEngine;

public class DXExceptionUtil {
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0030 A[SYNTHETIC, Splitter:B:25:0x0030] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0038 A[Catch:{ IOException -> 0x0034 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0043 A[SYNTHETIC, Splitter:B:34:0x0043] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x004b A[Catch:{ IOException -> 0x0047 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStackTrace(java.lang.Throwable r3) {
        /*
            if (r3 != 0) goto L_0x0005
            java.lang.String r3 = ""
            return r3
        L_0x0005:
            r0 = 0
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch:{ Throwable -> 0x002b, all -> 0x0028 }
            r1.<init>()     // Catch:{ Throwable -> 0x002b, all -> 0x0028 }
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch:{ Throwable -> 0x002c }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x002c }
            r3.printStackTrace(r2)     // Catch:{ Throwable -> 0x0026, all -> 0x0023 }
            java.lang.String r3 = r1.toString()     // Catch:{ Throwable -> 0x0026, all -> 0x0023 }
            r2.close()     // Catch:{ IOException -> 0x001e }
            r1.close()     // Catch:{ IOException -> 0x001e }
            goto L_0x0022
        L_0x001e:
            r0 = move-exception
            printStack(r0)
        L_0x0022:
            return r3
        L_0x0023:
            r3 = move-exception
            r0 = r2
            goto L_0x0041
        L_0x0026:
            r0 = r2
            goto L_0x002c
        L_0x0028:
            r3 = move-exception
            r1 = r0
            goto L_0x0041
        L_0x002b:
            r1 = r0
        L_0x002c:
            java.lang.String r3 = "DXExceptionUtil getStackTrace Exception"
            if (r0 == 0) goto L_0x0036
            r0.close()     // Catch:{ IOException -> 0x0034 }
            goto L_0x0036
        L_0x0034:
            r0 = move-exception
            goto L_0x003c
        L_0x0036:
            if (r1 == 0) goto L_0x003f
            r1.close()     // Catch:{ IOException -> 0x0034 }
            goto L_0x003f
        L_0x003c:
            printStack(r0)
        L_0x003f:
            return r3
        L_0x0040:
            r3 = move-exception
        L_0x0041:
            if (r0 == 0) goto L_0x0049
            r0.close()     // Catch:{ IOException -> 0x0047 }
            goto L_0x0049
        L_0x0047:
            r0 = move-exception
            goto L_0x004f
        L_0x0049:
            if (r1 == 0) goto L_0x0052
            r1.close()     // Catch:{ IOException -> 0x0047 }
            goto L_0x0052
        L_0x004f:
            printStack(r0)
        L_0x0052:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(java.lang.Throwable):java.lang.String");
    }

    public static void printStack(Throwable th) {
        if (DinamicXEngine.isDebug() && th != null) {
            th.printStackTrace();
        }
    }
}
