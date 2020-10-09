package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: U4Source */
final class k implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ UpdateTask b;

    k(UpdateTask updateTask, ValueCallback valueCallback) {
        this.b = updateTask;
        this.a = valueCallback;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006e A[Catch:{ Throwable -> 0x008b }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0070 A[Catch:{ Throwable -> 0x008b }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0079 A[Catch:{ Throwable -> 0x008b }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0097 A[Catch:{ Throwable -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0099 A[Catch:{ Throwable -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a5 A[Catch:{ Throwable -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00b7 A[Catch:{ Throwable -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ void onReceiveValue(java.lang.Object r15) {
        /*
            r14 = this;
            com.uc.webview.export.utility.download.DownloadTask r15 = (com.uc.webview.export.utility.download.DownloadTask) r15
            r0 = -1
            r1 = 100
            r3 = 0
            long r5 = r15.getTotalSize()     // Catch:{ Throwable -> 0x008b }
            r7 = 10
            int r9 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r9 != 0) goto L_0x0013
            r5 = r3
            goto L_0x001c
        L_0x0013:
            long r9 = r15.getCurrentSize()     // Catch:{ Throwable -> 0x008b }
            long r9 = r9 * r7
            long r9 = r9 / r5
            long r5 = r9 * r7
        L_0x001c:
            int r9 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r9 > 0) goto L_0x0027
            int r9 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r9 >= 0) goto L_0x0025
            goto L_0x0027
        L_0x0025:
            int r5 = (int) r5     // Catch:{ Throwable -> 0x008b }
            goto L_0x0028
        L_0x0027:
            r5 = -1
        L_0x0028:
            com.uc.webview.export.utility.download.UpdateTask r6 = r14.b     // Catch:{ Throwable -> 0x008b }
            int r6 = r6.g     // Catch:{ Throwable -> 0x008b }
            if (r5 > r6) goto L_0x0034
            r6 = 100
            if (r5 != r6) goto L_0x008f
        L_0x0034:
            com.uc.webview.export.utility.download.UpdateTask r6 = r14.b     // Catch:{ Throwable -> 0x008b }
            com.uc.webview.export.utility.download.UpdateTask r9 = r14.b     // Catch:{ Throwable -> 0x008b }
            int r9 = r9.g     // Catch:{ Throwable -> 0x008b }
            int r9 = r9 + 10
            int unused = r6.g = r9     // Catch:{ Throwable -> 0x008b }
            java.lang.String r6 = "sdk_ucm_p"
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Throwable -> 0x008b }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r5)     // Catch:{ Throwable -> 0x008b }
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x008b }
            java.lang.String r6 = r15.getFilePath()     // Catch:{ Throwable -> 0x008b }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x008b }
            long r9 = r5.getTotalSpace()     // Catch:{ Throwable -> 0x008b }
            long r5 = r5.getFreeSpace()     // Catch:{ Throwable -> 0x008b }
            r11 = 1048576(0x100000, double:5.180654E-318)
            long r11 = r5 / r11
            java.lang.String r13 = "sdk_ucm_dm"
            int r11 = (int) r11     // Catch:{ Throwable -> 0x008b }
            java.lang.String r11 = java.lang.String.valueOf(r11)     // Catch:{ Throwable -> 0x008b }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r13, r11)     // Catch:{ Throwable -> 0x008b }
            int r11 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r11 != 0) goto L_0x0070
            r5 = r3
            goto L_0x0075
        L_0x0070:
            long r5 = r5 * r7
            long r5 = r5 / r9
            long r5 = r5 * r7
        L_0x0075:
            int r7 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r7 > 0) goto L_0x0080
            int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r7 >= 0) goto L_0x007e
            goto L_0x0080
        L_0x007e:
            int r5 = (int) r5     // Catch:{ Throwable -> 0x008b }
            goto L_0x0081
        L_0x0080:
            r5 = -1
        L_0x0081:
            java.lang.String r6 = "sdk_ucm_dp"
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Throwable -> 0x008b }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r5)     // Catch:{ Throwable -> 0x008b }
            goto L_0x008f
        L_0x008b:
            r5 = move-exception
            r5.printStackTrace()
        L_0x008f:
            long r5 = r15.getTotalSize()     // Catch:{ Throwable -> 0x00c4 }
            int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r7 != 0) goto L_0x0099
            r5 = r3
            goto L_0x00a1
        L_0x0099:
            long r7 = r15.getCurrentSize()     // Catch:{ Throwable -> 0x00c4 }
            long r7 = r7 * r1
            long r5 = r7 / r5
        L_0x00a1:
            int r15 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r15 > 0) goto L_0x00ab
            int r15 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r15 >= 0) goto L_0x00aa
            goto L_0x00ab
        L_0x00aa:
            int r0 = (int) r5     // Catch:{ Throwable -> 0x00c4 }
        L_0x00ab:
            android.webkit.ValueCallback r15 = r14.a     // Catch:{ Throwable -> 0x00c4 }
            if (r15 == 0) goto L_0x00c3
            com.uc.webview.export.utility.download.UpdateTask r15 = r14.b     // Catch:{ Throwable -> 0x00c4 }
            int r15 = r15.h     // Catch:{ Throwable -> 0x00c4 }
            if (r0 <= r15) goto L_0x00c3
            com.uc.webview.export.utility.download.UpdateTask r15 = r14.b     // Catch:{ Throwable -> 0x00c4 }
            int unused = r15.h = r0     // Catch:{ Throwable -> 0x00c4 }
            android.webkit.ValueCallback r15 = r14.a     // Catch:{ Throwable -> 0x00c4 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r14.b     // Catch:{ Throwable -> 0x00c4 }
            r15.onReceiveValue(r0)     // Catch:{ Throwable -> 0x00c4 }
        L_0x00c3:
            return
        L_0x00c4:
            r15 = move-exception
            r15.printStackTrace()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.k.onReceiveValue(java.lang.Object):void");
    }
}
